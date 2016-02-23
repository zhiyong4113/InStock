package stock.awesome.instock;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class InputStockActivity extends AppCompatActivity {

    Calendar myCalendar;
    EditText expiryDate;
    DatabaseWriteProduct writer = new DatabaseWriteProduct(new Firebase("https://scorching-inferno-2190.firebaseio.com/"));
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_stock);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myCalendar = Calendar.getInstance();
        expiryDate = (EditText) findViewById(R.id.expiryEdit);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        expiryDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(InputStockActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        final Button addItem = (Button) findViewById(R.id.addNewItem);
        final Button updateItem = (Button) findViewById(R.id.updateItem);
        Button submitButton = (Button) findViewById(R.id.submitButton);

        //create it with add item button pressed already
        addItem.setPressed(true);

        //go to update item activity
        updateItem.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updateItem.setPressed(true);
                Intent intent = new Intent(context, UpdateItemActivity.class);
                startActivity(intent);
                return true;
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Product inputProd = new Product();
                onSubmitPress(inputProd);
                Log.d("Submit successful", inputProd.getName() + " " + inputProd.getQuantity() + " "
                        + CalendarAsStr.format(inputProd.getExpiry()));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        expiryDate.setText(sdf.format(myCalendar.getTime()));
    }


    // Called when submit button is pressed
    public void onSubmitPress(Product inputProd) {

        EditText inputId = (EditText) findViewById(R.id.productEdit);
        inputProd.setId(inputId.getText().toString());

        EditText inputName = (EditText) findViewById(R.id.nameEdit);
        inputProd.setName(inputName.getText().toString());

        EditText inputDesc = (EditText) findViewById(R.id.descriptionEdit);
        inputProd.setDesc(inputDesc.getText().toString());

        EditText inputQty = (EditText) findViewById(R.id.quantityEdit);
        inputProd.setQuantity(Integer.valueOf(inputQty.getText().toString()));

        EditText inputLocation = (EditText) findViewById(R.id.locationEdit);
        inputProd.setLocation(inputLocation.getText().toString());

        int inputExpiryYear = myCalendar.get(Calendar.YEAR);
        int inputExpiryMonth = myCalendar.get(Calendar.MONTH);
        int inputExpiryDate = myCalendar.get(Calendar.DATE);
        inputProd.setExpiry(new GregorianCalendar(inputExpiryYear, inputExpiryMonth, inputExpiryDate));

        writer.writeProduct(inputProd);
        // intent --> go to next activity, provide dialog box confirmation
    }




}

