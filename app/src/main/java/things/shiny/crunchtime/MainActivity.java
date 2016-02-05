package things.shiny.crunchtime;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String[] TYPES = new String[] {
            "Pushups",
            "Situps",
            "Squats",
            "Leg-lifts",
            "Planking",
            "Jumping Jacks",
            "Pullups",
            "Cycling",
            "Walking",
            "Jogging",
            "Swimming",
            "Stair-Climbing",
            "Calories"
    };

    private static final String[] REP_TYPES = new String[] {
            "Pushups",
            "Situps",
            "Squats",
            "Leg-lifts",
            "Pullups"
    };

    // amount of units required to burn 100 calories
    //  (100 / value) * given is the amount of calories burned
    // last value is calories
    private static final int[] CALORIES = new int[] {
            350,
            200,
            225,
            25,
            25,
            10,
            100,
            12,
            20,
            12,
            13,
            15,
            100
    };
    private static AutoCompleteTextView inputText;
    private static AutoCompleteTextView outputText;
    private static TextView numText;
    private static TextView resultText;
    private static String currUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize auto complete text views
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                TYPES
        );

        inputText = (AutoCompleteTextView) findViewById(R.id.inputText);
        outputText = (AutoCompleteTextView) findViewById(R.id.outputText);
        numText = (TextView) findViewById(R.id.numText);
        resultText = (TextView) findViewById(R.id.resultText);
        inputText.setAdapter(typeAdapter);
        outputText.setAdapter(typeAdapter);
        final List typesList = Arrays.asList(TYPES);
        final List rep_types = Arrays.asList(REP_TYPES);
        currUnit = "";

        Button crunchBtn = (Button) findViewById(R.id.crunchBtn);


        // set event handlers
        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String currVal = inputText.getText().toString();
                    TextView unitText = (TextView) findViewById(R.id.unitText);

                    if (typesList.contains(currVal)) {
                        // if calories set text field to empty
                        if (currVal.equals("Calories")) {
                            currUnit = "";
                        } else if (rep_types.contains(currVal)) {
                            currUnit = "rep(s) of";
                        } else {
                            currUnit = "minute(s) of";
                        }
                    } else {
                        if (!currVal.equals("Exercise / Calories")) {
                            currUnit = "(what?!)";
                        }
                    }
                    unitText.setText(currUnit);
                }
            }
        });
        crunchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currInput = inputText.getText().toString();
                String currOutput = outputText.getText().toString();

                float scale, toBurn100, toBurn100_2, result;
                String resultStr;


                // case 1 ) input : calories / output : calories
                if (currInput.equals(currOutput)) {
                    resultStr = String.format("Why are you wasting my time...");
                    resultText.setText(resultStr);
                } else if (typesList.contains(currInput) && typesList.contains(currOutput)) {

                    int inputNum = Integer.parseInt(numText.getText().toString());
                    int inIdx = typesList.indexOf(currInput);
                    int outIdx = typesList.indexOf(currOutput);
                    String resultUnit = currUnit;

                    if (currInput.equals("Calories")) { // case 2 ) input : calories / output : exercise
                        scale = (float) inputNum / 100f;
                        toBurn100 = (float) CALORIES[outIdx];
                        result = scale * toBurn100;
                        if (rep_types.contains(currOutput)) {
                            resultUnit = "rep(s) of";
                        } else {
                            resultUnit = "minute(s) of";
                        }
                        resultStr = String.format("You need to do \n%.2f %s %s \nto burn that!", result, resultUnit, currOutput.toLowerCase());
                    } else if (currOutput.equals("Calories")) { // case 3 ) input : exercise / output : calories
                        toBurn100 = (float) CALORIES[inIdx];
                        scale = (float) inputNum / toBurn100;
                        result = scale * 100f;
                        resultUnit = "";
                        resultStr = String.format("You burned \n%.2f %s %s!", result, resultUnit, currOutput.toLowerCase());
                    } else { // case 4 ) input : exercise / output : exercise
                        toBurn100 = CALORIES[inIdx];
                        toBurn100_2 = CALORIES[outIdx];
                        scale = (float) inputNum / toBurn100;
                        result = scale * toBurn100_2;
                        if (rep_types.contains(currOutput)) {
                            resultUnit = "rep(s) of";
                        } else {
                            resultUnit = "minute(s) of";
                        }
                        resultStr = String.format("You need to do \n%.2f %s %s!", result, resultUnit, currOutput.toLowerCase());
                    }

                    resultText.setText(resultStr);
                } else {
                    String errorMsg = "I don't know what '";
                    if (!typesList.contains(currInput)) {
                        errorMsg += currInput;
                    } else {
                        errorMsg += currOutput;
                    }
                    errorMsg += "' is!";
                    resultText.setText(errorMsg);
                }
            }
        });

    }
}
