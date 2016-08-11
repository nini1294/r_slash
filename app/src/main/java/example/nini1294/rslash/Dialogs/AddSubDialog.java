package example.nini1294.rslash.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.ButterKnife;
import example.nini1294.rslash.R;

/**
 * Created by Nishant on 01/05/16.
 */
public class AddSubDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_sub_dialog, null);
        EditText input = ButterKnife.findById(v, R.id.add_sub_edit_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
        builder.setMessage("Add a new subreddit");
        input.requestFocus();
        builder.setView(v);
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