package callisto.quotermvp.estatedetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import callisto.quotermvp.R;
import callisto.quotermvp.base.BaseFragment;
import callisto.quotermvp.estatedetails.mvp.EstateDetailsModel;
import callisto.quotermvp.estatedetails.mvp.EstateDetailsPresenter;
import callisto.quotermvp.estatedetails.mvp.EstateDetailsView;
import callisto.quotermvp.tools.BusProvider;

import static android.app.Activity.RESULT_OK;
import static callisto.quotermvp.tools.Constants.Strings.ESTATE_KEY;
import static callisto.quotermvp.tools.Constants.Values.RQ_PICK_CONTACT;

public class EstateDetailsFragment extends BaseFragment {
    public static EstateDetailsFragment newInstance(long estateKey) {
        EstateDetailsFragment fragment = new EstateDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ESTATE_KEY.getText(), estateKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        createPresenter(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQ_PICK_CONTACT.getValue() &&
            resultCode == RESULT_OK) {
            ((EstateDetailsPresenter) presenter).setContact(data.getData());
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_estate_details;
    }

    @Override
    protected void createPresenter(View view) {
        Bundle args = this.getArguments();
        long estateId = args.getLong(ESTATE_KEY.getText());

        presenter = new EstateDetailsPresenter(new EstateDetailsModel(estateId),
            new EstateDetailsView(this, view, BusProvider.getInstance()));
    }
}
