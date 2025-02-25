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
    private RadioGroup radioGroupTicketType;
    private RadioButton radioMultiple, radioSingle;
    private LinearLayout layoutTicketCount;
    private TextView tvTicketCount;
    private SeekBar seekBarTickets;
    private RadioGroup radioGroupPayment;
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        radioGroupTicketType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMultiple) {
                    layoutTicketCount.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioSingle) {
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
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        buttonFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // İsim boşsa Toast
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.empty_name),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int moviePos = spinnerMovies.getSelectedItemPosition();
                String selectedMovie = movieNames[moviePos];

                RadioButton radioPaypal = findViewById(R.id.radioPaypal);
                String paymentMethod = radioPaypal.isChecked() ? "Paypal" : "Visa";

                int ticketCount = 1; // Single varsayılan
                if (radioMultiple.isChecked()) {
                    ticketCount = seekBarTickets.getProgress() + 1;
                }

                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_ticket_details, null);
                dialog.setView(dialogView);

                TextView tvTicketInfo = dialogView.findViewById(R.id.tvTicketInfo);
                Button btnClose = dialogView.findViewById(R.id.btnClose);

                String info = "Movie : " + selectedMovie
                        + "\nPayment : " + paymentMethod
                        + "\nTicket Count : " + ticketCount
                        + "\nYour Name: " + name;

                tvTicketInfo.setText(info);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
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
