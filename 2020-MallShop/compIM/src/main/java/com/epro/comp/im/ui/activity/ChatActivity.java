package com.epro.comp.im.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.epro.comp.im.R;
import com.epro.comp.im.listener.ChatCloseEvent;
import com.epro.comp.im.listener.ChatMessageChangeEvent;
import com.epro.comp.im.listener.ConnectSuccessEvent;
import com.epro.comp.im.listener.SoftKeyBoardListener;
import com.epro.comp.im.mvp.contract.ChatContract;
import com.epro.comp.im.mvp.model.bean.AudioMsgBody;
import com.epro.comp.im.mvp.model.bean.ChatMessage;
import com.epro.comp.im.mvp.model.bean.CustomerService;
import com.epro.comp.im.mvp.model.bean.IMShop;
import com.epro.comp.im.mvp.model.bean.ImageMsgBody;
import com.epro.comp.im.mvp.model.bean.MsgBody;
import com.epro.comp.im.mvp.model.bean.MsgType;
import com.epro.comp.im.mvp.model.bean.Picture;
import com.epro.comp.im.mvp.presenter.ChatPresenter;
import com.epro.comp.im.service.IMService;
import com.epro.comp.im.ui.adapter.ChatAdapter;
import com.epro.comp.im.ui.adapter.ChatServiceAdapter;
import com.epro.comp.im.ui.widget.CustomerServicePopup;
import com.epro.comp.im.ui.widget.MediaManager;
import com.epro.comp.im.ui.widget.MessageHandlerDialog;
import com.epro.comp.im.ui.widget.RecordButton;
import com.epro.comp.im.ui.widget.StateButton;
import com.epro.comp.im.utils.ActivityUtil;
import com.epro.comp.im.utils.ChatUiHelper;
import com.epro.comp.im.utils.IMBusManager;
import com.epro.comp.im.utils.IMConst;
import com.epro.comp.im.utils.PictureFileUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.mike.baselib.activity.BaseRedTitleBarCustomActivity;
import com.mike.baselib.utils.AppBusManager;
import com.mike.baselib.utils.DisplayManager;
import com.mike.baselib.utils.NetworkUtil;
import com.mike.baselib.utils.SuperUtilsKt;
import com.mike.baselib.utils.ToastUtil;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class ChatActivity extends BaseRedTitleBarCustomActivity<ChatContract.View, ChatPresenter> implements View.OnClickListener, ChatContract.View {

    LinearLayout mLlContent;
    RecyclerView mRvChat;
    EditText mEtContent;
    RelativeLayout mRlBottomLayout;//表情,添加底部布局
    ImageView mIvAdd;
    ImageView mIvEmo;
    StateButton mBtnSend;//发送按钮
    ImageView mIvAudio;//录音图片
    RecordButton mBtnAudio;//录音按钮
    LinearLayout mLlEmotion;//表情布局
    LinearLayout mLlAdd;//添加布局
    private LinearLayout rootLayout;//整体View
    private ChatAdapter mAdapter;
    public static final int REQUEST_CODE_IMAGE = 0000;
    public static final int REQUEST_CODE_TAKE_PHOTO = 1111;
    public static final int REQUEST_CODE_FILE = 2222;
    public static final int RC_IM = 3333;
    private static final String TAG = "YB";
   // private static final String SHOP_ID = "shopId";
   //相片路径
   public Uri imageUri;
    //裁剪后路径
    public Uri cropUri;
    //裁剪
    public static final int CROP_REQUEST_CODE = 102;
    private void findView() {
        mLlContent = findViewById(R.id.llContent);
        mRvChat = findViewById(R.id.rv_chat_list);
        mEtContent = findViewById(R.id.et_content);
        mRlBottomLayout = findViewById(R.id.bottom_layout);
        mIvAdd = findViewById(R.id.ivAdd);
        mIvEmo = findViewById(R.id.ivEmo);
        mBtnSend = findViewById(R.id.btn_send);
        mIvAudio = findViewById(R.id.ivAudio);
        mBtnAudio = findViewById(R.id.btnAudio);
        mLlEmotion = findViewById(R.id.rlEmotion);
        mLlAdd = findViewById(R.id.llAdd);
        rootLayout = findViewById(R.id.rootLayout);
    }

    IMService.IMBinder imBinder;
    ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            imBinder = (IMService.IMBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public static void launchWithShop_CsId(Fragment fragment, IMShop shop, String csId, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), ChatActivity.class)
                .putExtra(IMShop.class.getSimpleName()+"_json",AppBusManager.Companion.toJson(shop)).putExtra("csId",csId), requestCode);
    }

    @Override
    public void initStatusBar() {
        //super.initStatusBar();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //修复屏幕点亮输入框下沉
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver,filter);
        softInputShow(this);
    }

    IMShop shop;

    boolean beforeKeyguard = false;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == Intent.ACTION_SCREEN_ON){
               // editTextHoldOn(beforeKeyguard);
            }else if (action == Intent.ACTION_SCREEN_OFF){
                //beforeKeyguard = isShow;
                //广播有延迟不同机型存在问题修改不了beforekeyguard状态，灭屏收起键盘。
                bindHelperView();
                helperView.hideBottomLayout(false);
                helperView.hideSoftInput();
            }
        }
    };

    @Override
    public void initView() {
        super.initView();
        SuperUtilsKt.ext_setRightImageResource(getTitleView(),R.mipmap.arrow,5);
        shop=AppBusManager.Companion.fromJson(getIntent().getStringExtra(IMShop.class.getSimpleName()+"_json"),IMShop.class);
        IMBusManager.Companion.saveIMShop(shop);
        getTitleView().setText(shop.getShopName());
        getRightView().setText(getString(R.string.into_shop));
        getRightView().setVisibility(View.VISIBLE);
        findView();
        initChatUi();
        mAdapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        mRvChat.setLayoutManager(mLinearLayout);
        mRvChat.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ChatMessage message = mAdapter.getItem(position);
                final boolean isSend = message.isSend();
                if (mAdapter.getItem(position).getMsgType() == MsgType.AUDIO) {
                    if (ivAudio != null) {
                        if (isSend) {
                            ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                        } else {
                            ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                        }
                        ivAudio = null;
                        MediaManager.reset();
                    } else {
                        ivAudio = view.findViewById(R.id.ivAudio);
                        MediaManager.reset();
                        if (isSend) {
                            ivAudio.setBackgroundResource(R.drawable.audio_animation_right_list);
                        } else {
                            ivAudio.setBackgroundResource(R.drawable.audio_animation_left_list);
                        }
                        AnimationDrawable drawable = (AnimationDrawable) ivAudio.getBackground();
                        drawable.start();
                        MediaManager.playSound(ChatActivity.this, ((AudioMsgBody) mAdapter.getData().get(position).getBody()).getOriginalPath(), new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if (isSend) {
                                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                                } else {
                                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                                }
                                MediaManager.release();
                            }
                        });
                    }
                } else if (mAdapter.getItem(position).getMsgType() == MsgType.IMAGE) {
                    ImageMsgBody imageMsgBody = (ImageMsgBody) message.getBody();
                    Picture picture = new Picture(imageMsgBody.getOriginalPath(), MsgType.IMAGE.name(), true);
                    PictureActivity.Companion.launchWithPictures(ChatActivity.this, picture, 0);
                }
            }
        });

        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                new MessageHandlerDialog.Builder(ChatActivity.this)
                        .setChatMessage(Objects.requireNonNull(mAdapter.getItem(position)))
                        .create().show();
                return true;
            }
        });
    }


    @Override
    public void initListener() {
        mBtnSend.setOnClickListener(this);
        findViewById(R.id.rlPhoto).setOnClickListener(this);
        findViewById(R.id.rlTakePhoto).setOnClickListener(this);
        findViewById(R.id.rlLocation).setOnClickListener(this);
        findViewById(R.id.rlFile).setOnClickListener(this);
        getRightView().setOnClickListener(this);
        getTitleView().setOnClickListener(this);
    }


    private ImageView ivAudio;

    private void initChatUi() {
        //mBtnAudio
        final ChatUiHelper mUiHelper = ChatUiHelper.with(this);
        mUiHelper.bindContentLayout(mLlContent)
                .bindttToSendButton(mBtnSend)
                .bindEditText(mEtContent, true)
                .bindBottomLayout(mRlBottomLayout)
                .bindEmojiLayout(mLlEmotion)
                .bindAddLayout(mLlAdd)
                .bindToAddButton(mIvAdd)
                .bindToEmojiButton(mIvEmo)
                .bindAudioBtn(mBtnAudio)
                .bindAudioIv(mIvAudio)
                .bindEmojiData();

        //底部布局弹出,聊天列表上滑
        mRvChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mRvChat.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mAdapter.getItemCount() > 0) {
                                mRvChat.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                            }
                        }
                    });
                }
            }
        });
        //点击空白区域关闭键盘
        mRvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mUiHelper.hideBottomLayout(false);
                mUiHelper.hideSoftInput();
                mEtContent.clearFocus();
                mIvEmo.setImageResource(R.mipmap.ic_emoji);
                return false;
            }
        });

        ((RecordButton) mBtnAudio).setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int time) {
                getLogTools().d("录音结束回调");
                File file = new File(audioPath);
                if (file.exists()) {
                    //sendAudioMessage(audioPath, time);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILE:
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    getLogTools().d("获取到的文件路径:" + filePath);
                    break;
                case REQUEST_CODE_IMAGE:
                    // 图片选择结果回调
                    List<LocalMedia> selectListPic = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListPic) {
                        Luban.with(this).load(media.getPath()).ignoreBy(100).setTargetDir("").setCompressListener(new OnCompressListener() {
                            @Override public void onStart() { }
                            @Override public void onSuccess(File file) { imBinder.sendFileMsg(file, MsgType.IMAGE); }
                            @Override public void onError(Throwable e) { ToastUtil.Companion.showToast(getString(R.string.image_compression));}}).launch();
                    }
                    break;
                case REQUEST_CODE_TAKE_PHOTO:
                    // 照相结果回调
                    cropUri = PictureFileUtil.startPhotoCrop(ChatActivity.this,imageUri, 0, 0, CROP_REQUEST_CODE);
                    break;
                case CROP_REQUEST_CODE:
                    //裁剪完成回调
                    Log.e("YB","图片路径："+cropUri.getPath());
                    Luban.with(this).load(cropUri.getPath()).ignoreBy(100).setTargetDir("").setCompressListener(new OnCompressListener() {
                        @Override public void onStart() {}
                        @Override public void onSuccess(File file) {imBinder.sendFileMsg(file, MsgType.IMAGE); }
                        @Override public void onError(Throwable e) {ToastUtil.Companion.showToast(R.string.image_compression);}}).launch();
                    break;
            }
        }
    }


    private void updateMessage(ChatMessage message) {
        int position = -1;
        //更新单个子条目
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            ChatMessage adapterMessage = mAdapter.getData().get(i);
            if (message.getUuid().equals(adapterMessage.getUuid())) {
                position = i;
            }
        }
        if (position >= 0) {
            if(message.isDelete()){
                mAdapter.remove(position);
                //修改bug删除消息后，输入框上移显示空白问题
                bindHelperView();
                mEtContent.post(new Runnable() {
                    @Override
                    public void run() {
                        helperView.hideBottomLayout(false);
                        helperView.hideSoftInput();
                    }
                });
            }else{
                mAdapter.notifyItemChanged(position);
            }
        } else {
            mAdapter.addData(message);
            mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        }
        setResult(Activity.RESULT_OK, new Intent().putExtra("shopId", shop.getShopId()));
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_send) {
            if (!isConnectSuccess || imBinder == null) {
                ToastUtil.Companion.showToast(getString(R.string.pls_reconnect_to_customer));
                return;
            }
            imBinder.sendTextMsg(mEtContent.getText().toString());
        } else if (view.getId() == R.id.rlPhoto) {
            if (!isConnectSuccess || imBinder == null) {
                ToastUtil.Companion.showToast(getString(R.string.pls_reconnect_to_customer));
                return;
            }
            PictureFileUtil.openGalleryPic(ChatActivity.this, REQUEST_CODE_IMAGE);
        } else if (view.getId() == R.id.rlTakePhoto) {
            imageUri =  PictureFileUtil.takePhoto(ChatActivity.this, REQUEST_CODE_TAKE_PHOTO);
        } else if (view.getId() == R.id.rlFile) {
            PictureFileUtil.openFile(ChatActivity.this, REQUEST_CODE_FILE);
        } else if (view.getId() == R.id.rlLocation) {

        } else if (view.getId() == getRightView().getId()) {
            ActivityUtil.Companion.launch2AppShopDetail(ChatActivity.this,
                    Objects.requireNonNull(shop.getShopId()));
        } else if (view.getId() == getTitleView().getId()) {
            showCustomerServicePop();
        }
    }

    @Override
    protected void onDestroy() {
        if (isBinded) {
            IMService.Companion.unbindService(this, mServiceConnection);
            isBinded = false;
        }

        ChatCloseEvent event = new ChatCloseEvent();
        String shopId = shop.getShopId();
        event.setShopId(shopId);
        EventBus.getDefault().post(event);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestIMPerm();
    }


    boolean isBinded = false;
    private void connectCustomerService() {
        if (AppBusManager.Companion.isLogin()) {
            if (NetworkUtil.isNetworkAvailable(this)) {
                IMService.Companion.startIMService(this, shop.getShopId(),csId);
                if (!isBinded) {
                    IMService.Companion.bindService(this, shop.getShopId(),csId, mServiceConnection);
                    isBinded = true;
                }
            } else {
                String shopId = shop.getShopId();
                getMPresenter().getMessageList(shopId, IMConst.GET_MESSAGE_LIST);
            }
        }
    }

    @NotNull
    @Override
    public ChatPresenter getPresenter() {
        return new ChatPresenter();
    }

    @Override
    public int layoutContentId() {
        return R.layout.activity_chat;
    }

    @AfterPermissionGranted(RC_IM)
    private void requestIMPerm() {
        if(TextUtils.isEmpty(csId)){
            return;
        }
        if (EasyPermissions.hasPermissions(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)) {
            connectCustomerService();
            // Already have permission, do the thing
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.request_more_per),
                    RC_IM,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO);
        }
    }

    boolean isConnectSuccess = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onChatMessageChangeEvent(ChatMessageChangeEvent event) {
        ChatMessage message = event.getMessage();
        assert message != null;
        if (message.getShopId().equals(shop.getShopId())) {
            updateMessage(message);
            if(!csId.equals(message.getCsId())){
                csId=message.getCsId();
                serviceName =IMBusManager.Companion.getCustomerService(csId).getName();
                isBinded = false;
                connectCustomerService();
            }
        }
        if (message.isSend()) {
            mEtContent.setText("");
        } else {

            mEtContent.setText(mEtContent.getText().toString());
            bindHelperView();
            if (isShow){
                helperView.showSoftInputAny();
            }else {
                helperView.hideBottomLayout(false);
                helperView.hideSoftInput();
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectSuccessEvent(ConnectSuccessEvent event) {
        String shopId = shop.getShopId();
        isConnectSuccess = true;
        //ToastUtil.Companion.showToast("与" +shopId+ "连线成功"+event.getTag());
        getTitleView().setText(shop.getShopName() +"(" +serviceName+")" );
        //普通消息接收监听
        getMPresenter().getMessageList(shopId, IMConst.GET_MESSAGE_LIST);
    }

    @Override
    public <T extends MsgBody> void onGetMessageListSuccess(@NotNull ArrayList<ChatMessage<T>> messageList) {
//        for (int i = 0; i <messageList.size() ; i++) {
//            getLogTools().toJson(messageList.get(i));
//        }
        mAdapter.getData().clear();
        for (int i = 0; i < messageList.size(); i++) {
            messageList.get(i).setRead(true);
        }
        mAdapter.addData(messageList);
        mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);

    }

    @Override
    public void onUploadImageSuccess(@NotNull ArrayList<String> sList) {

    }

    @Override
    public void initData() {
        csId=getIntent().getStringExtra("csId");
        if (csId!=null){
            serviceName = IMBusManager.Companion.getCustomerService(csId).getName();
        }
        String shopId = shop.getShopId();
        assert shopId != null;
        getMPresenter().getCustomerServiceList(shopId, IMConst.GET_CUSTOMER_SERVICE_LIST);
    }

    //聊天客服list
    public CustomerServicePopup servicePopup;
    public ChatServiceAdapter serviceAdapter;
    public void showCustomerServicePop() {
        servicePopup = new CustomerServicePopup(this);
        int height = DisplayManager.INSTANCE.getScreenHeight() * 20 / 100;
        int width = DisplayManager.INSTANCE.getScreenWidth() * 30 / 100;
        servicePopup.setHeight(height);
        servicePopup.setWidth(width);
        servicePopup.setBackgroundColor(getResources().getColor(R.color.transparent));
        servicePopup.setPopupGravity(Gravity.BOTTOM | Gravity.CENTER);
        servicePopup.showPopupWindow(getTitleBar());
        RecyclerView serviceRv = servicePopup.getContentView().findViewById(R.id.serviceRv);
        serviceAdapter = new ChatServiceAdapter(this);
        serviceRv.setLayoutManager(new LinearLayoutManager(this));
        serviceRv.setAdapter(serviceAdapter);
        if (mCustomerList!=null){
            serviceAdapter.getMData().clear();
            serviceAdapter.getMData().addAll(mCustomerList);
            serviceAdapter.notifyDataSetChanged();
        }
        serviceAdapter.setOnItemClickListener(new ChatServiceAdapter.OnItemClickListener() {
            @Override
            public void onClick(@NotNull CustomerService item, int position) {
                csId=item.getAccount();
                serviceName = item.getName();
                editTextHoldOn(isShow);
                isBinded = false;
                connectCustomerService();
                servicePopup.dismiss();
            }
        });
    }

    String csId = "";
    String serviceName ="";
    public ArrayList<CustomerService> mCustomerList;

    @Override
    public void onGetCustomerServiceListSuccess(@NotNull ArrayList<CustomerService> csList) {
        if (csList.size() > 0) {
            mCustomerList = csList;
            if(TextUtils.isEmpty(csId)){
                csId = csList.get(0).getAccount();
                serviceName = csList.get(0).getName();
                requestIMPerm();
            }
        }else{
           /*CustomerService cs1=new CustomerService("jason","","jason",getIntent().getStringExtra(SHOP_ID),"");
            CustomerService cs2=new CustomerService("id000000055","","id000000055",getIntent().getStringExtra(SHOP_ID),"");
            csList.add(cs1);
            csList.add(cs2);
            if(TextUtils.isEmpty(csId)){
                csId = csList.get(0).getAccount();
                requestIMPerm();
            }*/
        }
    }

    //键盘存在防止输入框下沉
    private boolean isShow = false;
    public void editTextHoldOn(boolean softShow){
        bindHelperView();
        if (softShow) {
            helperView.showSoftInputAny();
        }
    }

    public boolean softInputShow(Activity activity){
       SoftKeyBoardListener.setListener(activity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                isShow = true;
            }

            @Override
            public void keyBoardHide(int height) {
                isShow = false;
            }
        });
        return isShow;
    }

    public ChatUiHelper helperView;
    public void bindHelperView(){
        helperView = ChatUiHelper.with(ChatActivity.this);
        helperView.bindContentLayout(mLlContent)
                .bindttToSendButton(mBtnSend)
                .bindEditText(mEtContent, true)
                .bindBottomLayout(mRlBottomLayout)
                .bindEmojiLayout(mLlEmotion)
                .bindAddLayout(mLlAdd)
                .bindToAddButton(mIvAdd)
                .bindToEmojiButton(mIvEmo)
                .bindAudioBtn(mBtnAudio)
                .bindAudioIv(mIvAudio)
                .bindEmojiData();
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            Log.e("YB","hideSoftKeyboard");
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
