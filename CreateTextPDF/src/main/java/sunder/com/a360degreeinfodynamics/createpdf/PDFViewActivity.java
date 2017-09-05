package sunder.com.a360degreeinfodynamics.createpdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;
import java.io.File;
import java.util.List;

/**
 * Created by deepshikha on 29/5/17.
 */

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener,OnLoadCompleteListener {
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG="PDFViewActivity";
    int position=-1;
    private File file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        init();
    }

    private void init(){
        pdfView= (PDFView)findViewById(R.id.pdfview);
        position = getIntent().getIntExtra("position",-1);
        displayFromSdcard();
    }
    private void displayFromSdcard() {
        pdfFileName ="/sdcard/test.pdf";
         file = new File(pdfFileName);
      //  Toast.makeText(this, "Something wrong:"+file, Toast.LENGTH_LONG).show();
        Log.e("File path",file.getAbsolutePath());
        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    //    Toast.makeText(this, "Something wrong:1", Toast.LENGTH_LONG).show();
         setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
         ShareIt();

    }




   private void ShareIt() {
       File outputFile = new File("/sdcard/test.pdf");
       Uri uri = Uri.fromFile(outputFile);
       Intent share = new Intent();
       share.setAction(Intent.ACTION_SEND);
       share.setType("application/pdf");
       share.putExtra(Intent.EXTRA_STREAM, uri);
       startActivity(share);

   }




    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
          //  Toast.makeText(this, "Something wrong:2", Toast.LENGTH_LONG).show();
            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(PDFViewActivity.this,MainActivity.class));
        finish();

    }
}