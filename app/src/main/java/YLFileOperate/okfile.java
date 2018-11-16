package YLFileOperate;

import java.io.File;


/**
 * Created by Administrator on 2018/11/8.
 * https://blog.csdn.net/yiranaini_/article/details/53130512
 */
public  class okfile{
private void upFile(File file1){
//            /* 第一个要上传的file */
//            //File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/a.jpg");
//            RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file1);
//            String file1Name = "testFile1.txt";
//
//
//            /* form的分割线,自己定义 */
//            String boundary = "xx--------------------------------------------------------------xx";
//
//            MultipartBody mBody = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM)
//                    /* 上传一个普通的String参数 , key 叫 "p" */
//                    .addFormDataPart("p" , "你大爷666")
//                    /* 底下是上传了两个文件 */
//                    .addFormDataPart("file" , file1Name , fileBody1)
//                    .build();
//
//            /* 下边的就和post一样了 */
//            Request request = new Request.Builder().url("http://192.168.10.117:8080/test").post(mBody).build();
//                OkHttpClient client = new OkHttpClient();
//                client.newCall(request).enqueue(new Callback() {
//                public void onResponse(Call call, Response response) throws IOException {
//                    final  String bodyStr = response.body().string();
//                    final boolean ok = response.isSuccessful();
////                    runOnUiThread(new Runnable() {
////                        public void run() {
////                            if(ok){
////                                Toast.makeText(OKHttpActivity.this, bodyStr, Toast.LENGTH_SHORT).show();
////                            }else{
////                                Toast.makeText(OKHttpActivity.this, "server error : " + bodyStr, Toast.LENGTH_SHORT).show();
////                            }
////                        }
////                    });
//                }
//                public void onFailure(Call call, final IOException e) {
////                    runOnUiThread(new Runnable() {
////                        public void run() {
////                            Toast.makeText(OKHttpActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
////                        }
////                    });
//                }
//            });
        }

}
