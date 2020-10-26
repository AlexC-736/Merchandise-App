package com.jblearning.candystorev5;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private DatabaseManager dbManager;
  private double total;
  private ScrollView scrollView;
  private int buttonWidth;

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );
    Toolbar toolbar = ( Toolbar ) findViewById( R.id.toolbar );
    setSupportActionBar( toolbar );
    dbManager = new DatabaseManager( this );
    total = 0.0;
    scrollView = ( ScrollView ) findViewById( R.id.scrollView );
    Point size = new Point( );
    getWindowManager( ).getDefaultDisplay( ).getSize( size );
    buttonWidth = size.x / 2;
//    updateView( );
  }

//  protected void onResume( ) {
//    super.onResume( );
//    Toast.makeText( this, "OnResume", Toast.LENGTH_SHORT ).show( );
//    updateView( );
//  }

  protected void onStart( ) {
    super.onStart( );
    Toast.makeText( this, "OnStart", Toast.LENGTH_SHORT ).show( );
    updateView( );
  }

//  protected void onRestart( ) {
//    super.onRestart( );
//    Toast.makeText( this, "OnRestart", Toast.LENGTH_SHORT ).show( );
//    updateView( );
//  }


  public void updateView( ) {
    ArrayList<Candy> candies = dbManager.selectAll( );
    scrollView.removeAllViewsInLayout( );
    if( candies.size( ) > 0 ) {
      // remove subviews inside scrollView if necessary


      // set up the grid layout
      GridLayout grid = new GridLayout( this );
      grid.setRowCount( ( candies.size( ) + 1 ) / 2 );
      grid.setColumnCount( 2 );

      // create array of buttons, 2 per row
      CandyButton [] buttons = new CandyButton[candies.size( )];
      ButtonHandler bh = new ButtonHandler( );

      // fill the grid
      int i = 0;
      for ( Candy candy : candies ) {
        // create the button
        buttons[i] = new CandyButton( this, candy );
        buttons[i].setText( candy.getName( )
            + "\n" + candy.getPrice( ) );

        // set up event handling
        buttons[i].setOnClickListener( bh );

        // add the button to grid
        grid.addView( buttons[i], buttonWidth,
            GridLayout.LayoutParams.WRAP_CONTENT );
        i++;
      }

      Button checkout = new Button(this);
      checkout.setText("Check Out");
      ButtonHandlerTotal bht = new ButtonHandlerTotal( );
      checkout.setOnClickListener(bht);
      // add the button to grid
      grid.addView( checkout, buttonWidth,
              GridLayout.LayoutParams.WRAP_CONTENT );


      scrollView.addView( grid );
    }
  }

  //https://developer.android.com/reference/android/app/Activity#onCreateOptionsMenu(android.view.Menu)
  @Override
  public boolean onCreateOptionsMenu( Menu menu ) {
    getMenuInflater( ).inflate( R.menu.menu_main, menu );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId( );
    switch ( id ) {
      case R.id.action_add:
        Intent insertIntent
          = new Intent( this, InsertActivity.class );
        this.startActivity( insertIntent );
        return true;
      case R.id.action_delete:
        Intent deleteIntent
          = new Intent( this, DeleteActivity.class );
        this.startActivity( deleteIntent );
        return true;
      case R.id.action_update:
        Intent updateIntent
          = new Intent( this, UpdateActivity.class );
        this.startActivity( updateIntent );
        return true;
      case R.id.action_reset:
        total = 0.0;
        return true;
      case R.id.action_total:
        double revenue = dbManager.getTotal();
        String pay =
                NumberFormat.getCurrencyInstance( ).format( revenue );
//        Toast.makeText( MainActivity.this, "total revenue: "+ revenue,
//                Toast.LENGTH_LONG ).show( );

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Total revenue is "+pay);
        alertDialogBuilder.setPositiveButton("OK", null);
        alertDialogBuilder.show();
        return true;
      case R.id.action_year:
        dbManager.deleteTransactions();

      default:
        return super.onOptionsItemSelected( item );
    }
  }

  private class ButtonHandler implements View.OnClickListener {
    public void onClick( View v ) {
      // retrieve price of the candy and add it to total
      total += ( ( CandyButton ) v ).getPrice( );
      String pay =
        NumberFormat.getCurrencyInstance( ).format( total );
      Toast.makeText( MainActivity.this, pay,
          Toast.LENGTH_LONG ).show( );
    }
  }

  private class ButtonHandlerTotal implements View.OnClickListener {
    public void onClick( View v ) {
      dbManager.insertTotal( total );
      String pay =
              NumberFormat.getCurrencyInstance( ).format( total );
      Toast.makeText( MainActivity.this, "total saved: "+ total,
              Toast.LENGTH_LONG ).show( );
      total = 0;
    }
  }

}
