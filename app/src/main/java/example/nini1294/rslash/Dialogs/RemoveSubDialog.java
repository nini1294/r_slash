package example.nini1294.rslash.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.orhanobut.logger.Logger;
import com.pacoworks.rxpaper.RxPaperBook;

import java.util.ArrayList;
import java.util.List;

import example.nini1294.rslash.MainActivity;
import example.nini1294.rslash.R;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nishant on 01/05/16.
 */
public class RemoveSubDialog extends DialogFragment {

    CompositeSubscription sub;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RxPaperBook.init(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
        builder.setMessage("Remove a subreddit");
        sub = new CompositeSubscription();
        final List<String> list = new ArrayList<>();
        sub.add(RxPaperBook.with()
                .read(MainActivity.LIST_NAME, new ArrayList<String>())
                .doOnError((t) -> Logger.i(t.toString()))
                .subscribe(list::addAll));
        final TextView tv = new TextView(getActivity());
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_textview,
                list);
        final ListView lv = new ListView(getActivity());

        lv.setAdapter(adapter);
        sub.add(RxAdapterView.itemClicks(lv)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((pos) -> {
                    String item = list.get(pos);
                    ((SubRemovedListener) getActivity()).removeSub(item); // Call observer
                    this.dismiss();
                }));
        builder.setView(lv);
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            //Nothing
        });

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        sub.unsubscribe();
        super.onDestroyView();
    }

    public interface SubRemovedListener {
        void removeSub(String sub);
    }
}
