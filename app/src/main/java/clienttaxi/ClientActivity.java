package clienttaxi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import clienttaxi.R;

public class ClientActivity extends AppCompatActivity {
    private static final String TAG = ClientActivity.class.getSimpleName();
    public static final String  SHARED_NAME = "AddNewClientActivitysharedpreference";
    TextView clientname;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        SharedPreferences pref=getSharedPreferences(SHARED_NAME,MODE_PRIVATE);
        String EndUse_idStr=pref.getString("EndUse_id","");
        String enduser_nameStr=pref.getString("enduser_name","");
        Log.v(TAG," EndUse_idStr : "+EndUse_idStr+" , enduser_nameStr :"+enduser_nameStr);
        clientname=findViewById(R.id.clientname);
        if(enduser_nameStr== null || enduser_nameStr.equals(""))
        {


        }
        else
        {
            clientname.setText("اهلا وسهلا  "+enduser_nameStr);

        }

    }

    public void go_to_add_new_client_activty(View view) {
        startActivity(new Intent(ClientActivity.this, AddNewClientActivity.class));

    }

    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences pref=getSharedPreferences(AddNewOfficeActivity.SHARED_NAME,MODE_PRIVATE);
        String EndUse_idStr=pref.getString("EndUse_id","");
        String enduser_nameStr=pref.getString("enduser_name","");
        Log.v(TAG," EndUse_idStr : "+EndUse_idStr+" , enduser_nameStr :"+enduser_nameStr);
        clientname=findViewById(R.id.clientname);

        if(enduser_nameStr == null || enduser_nameStr.equals(""))
        {


        }
        else
        {
            clientname.setText("اهلا وسهلا  "+enduser_nameStr);

        }
    }

    public void show_all_taxi_for_office_idClick(View view)
    {


    }


    public void show_all_officce(View view)
    {

        startActivity(new Intent(ClientActivity.this, SelectOffiecesActivity.class));

    }
}
