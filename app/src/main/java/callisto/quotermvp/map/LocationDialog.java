package callisto.quotermvp.map;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import callisto.quotermvp.R;
import callisto.quotermvp.map.mvp.CustomMapPresenter;
import callisto.quotermvp.tools.BusProvider;
import callisto.quotermvp.tools.Events;

public class LocationDialog extends DialogFragment {
    @BindView(R.id.editAddress) EditText editAddress;

    private String title;
    private Bus bus;
    private CustomMapPresenter mapPresenter;

    private Unbinder unbinder;

    public static LocationDialog newInstance(String title, CustomMapPresenter customMapPresenter) {
        LocationDialog fragment = new LocationDialog();
        fragment.title = title;
        fragment.mapPresenter = customMapPresenter;
        fragment.bus = BusProvider.getInstance();
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout root = (LinearLayout) inflater.inflate(getLayout(), null);

        unbinder = ButterKnife.bind(this, root);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(root);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                performDialogClosing();
            }
        });

        builder.setTitle(title);

        AlertDialog dialog = builder.create();

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.register(this);
    }

    @Override
    public void onPause() {
        BusProvider.unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void performDialogClosing() {
        bus.post(new Events.GeocodingRequestEvent(editAddress.getText().toString()));

//        mapPresenter.fireGeocodingRequest(editAddress.getText().toString());
        dismiss();
    }

    private int getLayout() {
        return R.layout.dialog_location;
    }
}
