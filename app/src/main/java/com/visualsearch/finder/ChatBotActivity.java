package com.visualsearch.finder;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.visualsearch.finder.Adapter.ChatMessageAdapter;
import com.visualsearch.finder.Model.ChatMessage;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class ChatBotActivity extends AppCompatActivity {


    ListView listView;
    FloatingActionButton floatingActionButton;
    EditText edtTextMessage;
    ImageView imageView;

    Button btnSend;
//    public Bot bot;
//    public static Chat chat;
    private ChatMessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);


        listView = findViewById(R.id.listview);
        btnSend = findViewById(R.id.btnSend);
        edtTextMessage = findViewById(R.id.edtTextMessage);
        imageView = findViewById(R.id.imageView);



        adapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        listView.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String message = edtTextMessage.getText().toString();

//                String response = chat.multisentenceRespond(edtTextMessage.getText().toString());
//                if(TextUtils.isEmpty(message)){
//
//                    Toast.makeText(getApplicationContext(),"Enter message",Toast.LENGTH_SHORT).show();
//
//                    return;
//                }
//
//                sandMessage(message);
//                botsReply(response);
//                edtTextMessage.setText(" ");
//                listView.setSelection(adapter.getCount() -1);



            }
        });



        Dexter.withContext(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        if(multiplePermissionsReport.areAllPermissionsGranted()){
                            custom();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {



                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check();


        //custom();

    }

    private void custom(){

        boolean available = isSDCARDAvailable();

        AssetManager assets = getResources().getAssets();
        File filename = new File(Environment.getExternalStorageDirectory().toString()+"/TBC/bots/finderchatbot");


        boolean makeFIle = filename.mkdir();



        if(filename.exists()){


            try{
                for(String dir:assets.list("finderchatbot")){


                    File subDir  =new File(filename.getPath()+"/"+dir);

                    boolean subDir_check = subDir.mkdir();

                    for(String file: assets.list("finderchatbot/"+dir)){

                        File newFile = new File(filename.getPath()+"/"+dir+"/"+file);
                        if(newFile.exists()){

                            continue;
                        }
                        InputStream in;
                        OutputStream out;
                        in = assets.open("finderchatbot/"+dir+"/"+file);
                        out = new FileOutputStream(filename.getPath()+"/"+dir+"/"+file);
                        copyFile(in,out);
                        in.close();
                        out.flush();
                        out.close();
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getApplicationContext(),"nOT eXIST",Toast.LENGTH_SHORT).show();
        }

//
//        MagicStrings.root_path =Environment.getExternalStorageDirectory().toString()+"/TBC";
//        AIMLProcessor.extension = new PCAIMLProcessorExtension();
//
//        bot = new Bot("finderchatbot",MagicStrings.root_path,"chat");
//        chat=new Chat(bot);
//


    }

    private boolean isSDCARDAvailable() {

        return  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? true : false;


    }

    private void copyFile(InputStream in, OutputStream out) throws  IOException{

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer))!= -1){

            out.write(buffer,0,read);
        }

    }

    private void sandMessage(String message) {

        ChatMessage chatMessage = new ChatMessage(message,true,false);
        adapter.add(chatMessage);
    }

    private void botsReply(String reply) {
        ChatMessage chatMessage = new ChatMessage(reply,false,false);
        adapter.add(chatMessage);


    }


}