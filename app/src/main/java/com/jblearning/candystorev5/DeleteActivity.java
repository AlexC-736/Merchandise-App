package com.jblearning.candystorev5;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {
  private DatabaseManager dbManager;

  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    dbManager = new DatabaseManager( this );
    updateView( );
  }

  // Build a View dynamically with all the candies
  public void updateView( ) {
    ArrayList<Candy> candies = dbManager.selectAll( );
    RelativeLayout layout = new RelativeLayout( this );
    ScrollView scrollView = new ScrollView( this );
    RadioGroup group = new RadioGroup( this );
    for ( Candy candy : candies ) {
      RadioButton rb = new RadioButton( this );
      rb.setId( candy.getId( ) );
      rb.setText( candy.toString( ) );
      group.addView( rb );
    }
    // set up event handling
    RadioButtonHandler rbh = new RadioButtonHandler( );
    group.setOnCheckedChangeListener(rbh);

    // create a back button
    Button backButton = new Button( this );
    backButton.setText( R.string.button_back );

    //Create an Okay button

    backButton.setOnClickListener( new View.OnClickListener( ) {
      public void onClick(View v) {
        DeleteActivity.this.finish();
      }
    });

    scrollView.addView(group);
    layout.addView( scrollView );

    // add back button at bottom
    RelativeLayout.LayoutParams params
        = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT );
    params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
    params.addRule( RelativeLayout.CENTER_HORIZONTAL );
    params.setMargins( 50, 0, 50, 50 );
    layout.addView( backButton, params );

    //add okay button at the bottom above back button




    setContentView( layout );
  }

  private class RadioButtonHandler
    implements RadioGroup.OnCheckedChangeListener {
    public void onCheckedChanged( RadioGroup group, final int checkedId ) {
      //RadioButton rb = (RadioButton) group.findViewbyId(checkedId);
      //delete candy from database
      //Toast.makeText( DeleteActivity.this, +checkedId,
        //      Toast.LENGTH_SHORT ).show( );
      AlertDialog.Builder builder = new AlertDialog.Builder(DeleteActivity.this);
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          Toast.makeText( DeleteActivity.this, ""+checkedId,
                  Toast.LENGTH_SHORT ).show( );
          dbManager.deleteById( checkedId );
          updateView();

        }

      });
      builder.setNegativeButton("cancel", null);
      builder.setMessage("Would you like delete this?");


      AlertDialog alert = builder.create();
      alert.show();
    }
  }

}
