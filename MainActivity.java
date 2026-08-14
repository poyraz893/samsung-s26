package com.example.s26basilitut;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.widget.*;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(40,40,40,40);

        TextView t = new TextView(this);
        t.setText("S26 BASILI TUT\n\n1) Erişilebilirliği açın.\n2) Hizmeti etkinleştirin.\n3) BASILI TUT düğmesini gösterin.");
        t.setTextSize(19);

        Button settings = new Button(this);
        settings.setText("Erişilebilirliği Aç");
        settings.setOnClickListener(v ->
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));

        Button show = new Button(this);
        show.setText("BASILI TUT BUTONUNU GÖSTER");
        show.setOnClickListener(v -> {
            if (HoldAccessibilityService.instance != null)
                HoldAccessibilityService.instance.showOverlay();
            else
                Toast.makeText(this, "Önce Erişilebilirlik hizmetini etkinleştirin.", Toast.LENGTH_LONG).show();
        });

        l.addView(t);
        l.addView(settings);
        l.addView(show);
        setContentView(l);
    }
}