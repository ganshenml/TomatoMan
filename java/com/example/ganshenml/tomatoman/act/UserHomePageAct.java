package com.example.ganshenml.tomatoman.act;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganshenml.tomatoman.R;
import com.example.ganshenml.tomatoman.bean.Person;
import com.example.ganshenml.tomatoman.callback.HttpCallback;
import com.example.ganshenml.tomatoman.tool.CommonUtils;
import com.example.ganshenml.tomatoman.tool.FileTool;
import com.example.ganshenml.tomatoman.tool.LogTool;
import com.example.ganshenml.tomatoman.tool.ShowDialogUtils;
import com.example.ganshenml.tomatoman.tool.StringTool;
import com.example.ganshenml.tomatoman.tool.ThreadTool;
import com.example.ganshenml.tomatoman.tool.ToActivityPage;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserHomePageAct extends BaseActivity {
    public final int TAKE_PHOTO = 1;//拍照
    public final int CROP_PHOTO = 2;//裁剪
    public final int CHOOSE_PHOTO = 3;//从相册选择

    public final String TAG = "UserHomePageAct";

    private Toolbar userHomeTb;
    private ImageView backIv,ivMyTomatoHomepage, ivMyFriendsHomepage;
    private TextView updatePassTv,tvUserNameHomepage, tvUserIntroHomepage;
    private Context thisContext = UserHomePageAct.this;
    private Uri imageUri;
    private ProgressDialog progressDialog;//进度对话框
    private SimpleDraweeView simpleDraweeView_user_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_home_page);
        initViews();
        initData();
        initListeners();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------以下为自定义方法--------------------------------------------------------

    private void initViews() {

        //初始并实例化化控件
        updatePassTv = (TextView) findViewById(R.id.updatePassTv);
        tvUserNameHomepage = (TextView) findViewById(R.id.tvUserNameHomepage);
        tvUserIntroHomepage = (TextView) findViewById(R.id.tvUserIntroHomepage);
        simpleDraweeView_user_log = (SimpleDraweeView) findViewById(R.id.simpleDraweeView_user_log);
        Person person = BmobUser.getCurrentUser(Person.class);
        String picUrlTemp = person.getImageId();
        if(!StringTool.isEmpty(picUrlTemp)){
            simpleDraweeView_user_log.setImageURI(picUrlTemp);
        }

        userHomeTb = (Toolbar)findViewById(R.id.userHomeTb);
        userHomeTb.getBackground().setAlpha(0);
        backIv = (ImageView) findViewById(R.id.backIv);
        ivMyTomatoHomepage = (ImageView) findViewById(R.id.ivMyTomatoHomepage);
        ivMyFriendsHomepage = (ImageView) findViewById(R.id.ivMyFriendsHomepage);

        tvUserNameHomepage.setText(person.getUsername());
        String userIntroStr = person.getIntroduction();
        if (!StringTool.isEmpty(userIntroStr)) {
            tvUserIntroHomepage.setText(userIntroStr);
        }
    }

    private void initData(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }
    private void initListeners() {

        //返回
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //修改密码
        updatePassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToActivityPage.turnToSimpleAct(UserHomePageAct.this,UpdatePassAct.class);
            }
        });

        //点击头像
        simpleDraweeView_user_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CommonUtils.judgeNetWork(UserHomePageAct.this)){//如果当前网络不可用
                    return;
                }

                ShowDialogUtils.showLogoItem(thisContext, new HttpCallback() {
                    @Override
                    public void onSuccess(Object data, String resultStr) {
                        upLoadPicFile(resultStr);//做上传图像处理
                    }
                });
            }
        });

        //点击签名档
        tvUserIntroHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userIntroStr = tvUserIntroHomepage.getText().toString();
                ShowDialogUtils.showInputTextDialog(thisContext, "编辑简介",userIntroStr, new HttpCallback() {
                    @Override
                    public void onSuccess(Object data, String resultStr) {
                        if (!StringTool.isEmpty(resultStr)) {
                            Person personTemp = new Person();
                            personTemp.setIntroduction(resultStr);
                            savePersonData(personTemp);
                        }
                    }
                });
            }
        });

        //点击“我的番茄”
        ivMyTomatoHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomePageAct.this,MyTomatoAct.class);
                startActivity(intent);
                finish();
            }
        });

        //点击“我的好友”
        ivMyFriendsHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(UserHomePageAct.this,MyFriendsAct.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 保存用户数据至服务器（当前仅保存用户简介）
     *
     * @param person
     */
    private void savePersonData(final Person person) {
        final BmobUser bmobUser = BmobUser.getCurrentUser();
        person.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ThreadTool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(thisContext, "保存成功", Toast.LENGTH_SHORT).show();
                            if (!StringTool.isEmpty(person.getIntroduction())) {//如果不为空，则表示要更新的是该条数据
                                tvUserIntroHomepage.setText(person.getIntroduction());
                            }
                            if (!StringTool.isEmpty(person.getImageId())) {//如果不为空，则表示要更新的是用户头像地址成功，进而本地数据进行更新
                                Person personTemp = BmobUser.getCurrentUser(Person.class);
                                personTemp.setImageId(person.getImageId());
                                simpleDraweeView_user_log.setImageURI(person.getImageId());
                            }
                        }
                    });
                } else {
                    Toast.makeText(thisContext, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 做上传头像图片处理
     *
     * @param resultStr：用户选择的上传方式数值
     */
    private void upLoadPicFile(String resultStr) {
        switch (resultStr) {
            case "0":
                //查看大图
                break;
            case "1":
                //从相册选择
                doTakePhotoOperation();
                break;
            case "2":
                //拍照
                doCaptureOperation();
                break;
            default:
                break;
        }
    }

    /**
     * 用户选择拍照方式上传
     */
    private void doCaptureOperation() {
        File outputImage = new File(Environment.getExternalStorageDirectory(),
                "output_image.jpg");
        LogTool.log(LogTool.Aaron,TAG+" outputImageUrl: "+Environment.getExternalStorageDirectory().toString());
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 用户选择从相册上传方式
     */
    private void doTakePhotoOperation(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");//选择“图片”这种类型

        startActivityForResult(intent, CHOOSE_PHOTO);

    }

    //对系统返回的动作结果进行分类处理：
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO://如果是拍照类型——>进行拍照动作
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO://如果是裁剪图片类型——>1.进行图片的裁剪动作；2.将该裁剪的图片上传至服务器
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));

                        uploadFile(imageUri.getPath());//上传图片

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO://如果是从相册选择图片的类型——>调用不同版本的方法进行处理：将该选择的图片上传至服务器
                if (resultCode == RESULT_OK) {
                    //判断手机版本号进行方法处理（以4.4版本（也就是代号19）为分界）
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下系统
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    //获取图片真实路径的方法
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {//如果能查到结果
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    //上传图片
    private void uploadFile(String imagePath) {

        progressDialog.setTitle("上传进度");
        progressDialog.setMessage("当前进度： " + 0 + " %");
        progressDialog.show();

//        final BmobFile bmobFile = new BmobFile(new File(imagePath));
        final BmobFile bmobFile = new BmobFile(FileTool.returnCompressedPic(Uri.parse(imagePath)));
        LogTool.log(LogTool.Aaron, TAG + " imagePath: " + imagePath);

        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {

                if (e == null) {
                    String picServerUrl = bmobFile.getFileUrl();

                    Person personTemp = new Person();
                    personTemp.setImageId(picServerUrl);
                    savePersonData(personTemp);//1.更新服务器用户的该属性数据；2.本地保存该上传的图片在服务器端的地址并显示

                    progressDialog.dismiss();//成功上传后取消进度对话框
                    LogTool.log(LogTool.Aaron, TAG + " 图片地址:" + picServerUrl);
                    Toast.makeText(thisContext, "上传成功", Toast.LENGTH_SHORT).show();

                } else {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(thisContext, "上传失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer value) {
                progressDialog.setMessage("当前进度： " + value + " %");
            }
        });

        //15秒后无论是否长传成功都取消掉弹窗显示
        ThreadTool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },15*1000);

    }



    //4.4以前的系统处理方法
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);

        uploadFile(imagePath);//上传图片

    }


    //4.4以后的系统处理方法
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则通过 document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的ID
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果不是document类型的Uri，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            }

            uploadFile(imagePath);//上传图片

        }
    }



}
