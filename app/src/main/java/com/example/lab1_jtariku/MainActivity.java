package com.example.lab1_jtariku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final BigDecimal PRICE_LARGE   = new BigDecimal("22.99");
    private static final BigDecimal PRICE_MEDIUM  = new BigDecimal("20.99");
    private static final BigDecimal PRICE_SMALL   = new BigDecimal("18.99");
    private static final BigDecimal TAX_RATE      = new BigDecimal("0.04");
    private static final BigDecimal SHIPPING_FLAT = new BigDecimal("2.50");

    private EditText qtyLarge, qtyMedium, qtySmall;
    private RadioButton shippingBtn, noShippingBtn;
    private TextView itemSubtotalTV, taxTV, shippingTV, totalTV;

    private final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        qtyLarge = findViewById(R.id.large_sweatshirt_quantity);
        qtyMedium = findViewById(R.id.medium_sweatshirt_quantity);
        qtySmall = findViewById(R.id.small_sweatshirt_quantity);

        shippingBtn = findViewById(R.id.shipping_button);
        noShippingBtn = findViewById(R.id.no_shipping_button);

        Button computeBtn = findViewById(R.id.compute_cost);

        itemSubtotalTV = findViewById(R.id.item_subtotal);
        taxTV = findViewById(R.id.tax);
        shippingTV = findViewById(R.id.shipping_cost);
        totalTV = findViewById(R.id.total_cost);

        computeBtn.setOnClickListener(v -> computeAndDisplay());
    }

    private void computeAndDisplay() {
        int qL = parseIntSafe(qtyLarge.getText() == null ? null : qtyLarge.getText().toString());
        int qM = parseIntSafe(qtyMedium.getText() == null ? null : qtyMedium.getText().toString());
        int qS = parseIntSafe(qtySmall.getText() == null ? null : qtySmall.getText().toString());

        BigDecimal subtotal = PRICE_LARGE.multiply(BigDecimal.valueOf(qL))
                .add(PRICE_MEDIUM.multiply(BigDecimal.valueOf(qM)))
                .add(PRICE_SMALL.multiply(BigDecimal.valueOf(qS)));

        BigDecimal tax = subtotal.multiply(TAX_RATE);
        BigDecimal shipping = shippingBtn.isChecked() ? SHIPPING_FLAT: BigDecimal.ZERO;

        BigDecimal total = subtotal.add(tax).add(shipping);

        itemSubtotalTV.setText(currency.format(subtotal));
        taxTV.setText(currency.format(tax));
        shippingTV.setText(currency.format(shipping));
        totalTV.setText(currency.format(total));
    }

    private int parseIntSafe(String s) {
        if (s == null) {
            return 0;
        }
        s = s.trim();
        if (s.isEmpty()) {
            return 0;
        }
        try {
            int v = Integer.parseInt(s);
            if (v > 10000) {
                throw new NumberFormatException("Quantity selected is too large");
            }
            return Math.max(v, 0); //this makes sure there is no negative quantity
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
}