package com.example.laba4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;

public class FileUtils {
    private static final String DIRECTORY_NAME = "DownloadedJournals";

    public static File getFile(Context context, String journalId) {
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), DIRECTORY_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, journalId + ".pdf");
    }

    public static void openPdf(Context context, File file) {
        try {
            Uri fileUri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "No apps for opening PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
