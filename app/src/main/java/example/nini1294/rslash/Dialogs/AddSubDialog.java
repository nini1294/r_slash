package example.nini1294.rslash.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import example.nini1294.rslash.MainActivity;

/**
 * Created by Nishant on 01/05/16.
 */
public class AddSubDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Add a new subreddit");
        final EditText input = new EditText(getActivity());
        input.setSingleLine(true);
        input.requestFocus();
        builder.setView(input);
        builder.setPositiveButton("Add", (dialog, which) -> {
            ((SubAddedListener) getActivity()).addSub(input.getText().toString());
        }).setNegativeButton("Cancel", (dialog, which) -> {
            // Nothing
        });
        return builder.create();
    }

    public interface SubAddedListener {
        void addSub(String sub);
    }
}