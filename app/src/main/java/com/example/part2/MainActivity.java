package com.example.part2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerMovies;
    private ImageView imageViewMovie;
    private RadioGroup radioGroupTicketType, radioGroupPayment;
    private RadioButton radioMultiple, radioSingle;
    private LinearLayout layoutTicketCount;
    private TextView tvTicketCount;
    private SeekBar seekBarTickets;
    private EditText etName;
    private Button buttonFinish, buttonClear;

    private int[] movieImages = {
            R.drawable.picture1,
            R.drawable.picture2,
            R.drawable.picture3,
            R.drawable.picture4
    };

    private String[] movieNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMovies      = findViewById(R.id.spinnerMovies);
        imageViewMovie     = findViewById(R.id.imageViewMovie);
        radioGroupTicketType = findViewById(R.id.radioGroupTicketType);
        radioMultiple      = findViewById(R.id.radioMultiple);
        radioSingle        = findViewById(R.id.radioSingle);
        layoutTicketCount  = findViewById(R.id.layoutTicketCount);
        tvTicketCount      = findViewById(R.id.tvTicketCount);
        seekBarTickets     = findViewById(R.id.seekBarTickets);
        radioGroupPayment  = findViewById(R.id.radioGroupPayment);
        etName             = findViewById(R.id.etName);
        buttonFinish       = findViewById(R.id.buttonFinish);
        buttonClear        = findViewById(R.id.buttonClear);

        movieNames = getResources().getStringArray(R.array.movies_array);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.movies_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMovies.setAdapter(adapter);

        spinnerMovies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageViewMovie.setImageResource(movieImages[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        radioGroupTicketType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMultiple) {
                    layoutTicketCount.setVisibility(View.VISIBLE);
                } else {
                    layoutTicketCount.setVisibility(View.GONE);
                }
            }
        });

        seekBarTickets.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int count = progress + 1;
                tvTicketCount.setText("Ticket Count (" + count + ")");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.name_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int moviePos = spinnerMovies.getSelectedItemPosition();
                String selectedMovie = movieNames[moviePos];

                RadioButton radioPaypal = findViewById(R.id.radioPaypal);
                String paymentMethod = radioPaypal.isChecked() ? "Paypal" : "Visa";

                int ticketCount = 1;
                if (radioMultiple.isChecked()) {
                    ticketCount = seekBarTickets.getProgress() + 1;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.sinema); // res/drawable/cinema.png
                builder.setTitle(getString(R.string.ticket_details_title));

                String msg = "Movie : " + selectedMovie
                        + "\nPayment : " + paymentMethod
                        + "\nTicket Count : " + ticketCount
                        + "\nYour Name: " + name;

                builder.setMessage(msg);

                builder.setPositiveButton(getString(R.string.close_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.show();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                spinnerMovies.setSelection(0);
                imageViewMovie.setImageResource(movieImages[0]);
                radioGroupTicketType.clearCheck();
                layoutTicketCount.setVisibility(View.GONE);
                seekBarTickets.setProgress(0);
                ((RadioButton)findViewById(R.id.radioPaypal)).setChecked(true);
                etName.setText("");
            }
        });
    }
}
