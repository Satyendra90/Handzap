package com.handzapassignment.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.handzapassignment.DateFormatUtils;
import com.handzapassignment.R;
import com.handzapassignment.adapter.CategoryAdapter;
import com.handzapassignment.adapter.PostAttachmentAdapter;
import com.handzapassignment.model.Category;
import com.handzapassignment.model.PostMedia;
import com.handzapassignment.utils.ItemOffsetDecoration;
import com.handzapassignment.utils.MediaUtils;
import com.handzapassignment.utils.PermissionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * Will provide interface for creating new post
 *
 * @author Satyendra
 * @version 1.0
 */
public class NewPostActivity extends BaseActivity {

    @BindView(R.id.txtPostTitle)
    TextInputLayout tilPostTitle;
    @BindView(R.id.txtPostDescription)
    TextInputLayout tilPostDescription;
    @BindView(R.id.txtPostTitleCounter)
    TextView txtPostTitleCounter;
    @BindView(R.id.txtPostDescCounter)
    TextView txtPostDescCounter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.etStartDate)
    EditText etStartDate;
    @BindView(R.id.etRate)
    EditText etRate;
    @BindView(R.id.etPaymentMethod)
    EditText etPaymentMethod;
    @BindView(R.id.etJobTerm)
    EditText etJobTerm;
    @BindView(R.id.etCategories)
    EditText etCategories;
    @BindView(R.id.etPostLocation)
    EditText etPostLocation;
    @BindView(R.id.etInr)
    EditText etInr;
    @BindView(R.id.imgCountry)
    ImageView imgCountry;
    @BindView(R.id.linearLayoutFocus)
    LinearLayout linearLayoutFocus;

    @BindView(R.id.etPostTitle)
    TextInputEditText etPostTitle;
    @BindView(R.id.etPostDescription)
    TextInputEditText etPostDescription;
    @BindView(R.id.rcAttachment)
    RecyclerView rcAttachment;

    private int maxPostLength, maxPostDescLength;
    private final String DATE_RANGE_FORMAT = "EEEE dd MMM";
    private String[] rates, paymentMethods, jobTerms;
    private boolean isAllRequiredFieldFilled;
    private ArrayList<PostMedia> mPostMediaList = new ArrayList<>();
    private PostAttachmentAdapter mAdapter;

    public static final int PICK_CATEGORY = 1;
    public static final int PICK_LOCATION = 2;
    public static final String PARAM_SELECTED_CATEGORY = "selected_category";
    public static final String PARAM_SELECTED_LOCATION = "selected_location";

    public interface OnItemSelectedListener {
        void onItemSelected(String item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);

        initializeToolbar(toolbar, getString(R.string.new_post));
        init();
        configureRecyclerView();
    }

    private void init() {
        Resources res = getResources();
        maxPostLength = res.getInteger(R.integer.post_title_max_length);
        maxPostDescLength = res.getInteger(R.integer.post_description_max_length);
        linearLayoutFocus.requestFocus();

        rates = res.getStringArray(R.array.rate);
        paymentMethods = res.getStringArray(R.array.payment_method);
        jobTerms = res.getStringArray(R.array.job_term);
    }

    private void configureRecyclerView() {
        rcAttachment.setHasFixedSize(true);
        LinearLayoutManager llManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcAttachment.setLayoutManager(llManager);

        mAdapter = new PostAttachmentAdapter(this);
        rcAttachment.setAdapter(mAdapter);
        mAdapter.setRecyclerViewReference(rcAttachment);
    }

    @OnClick({R.id.etRate, R.id.etPaymentMethod, R.id.etPostLocation, R.id.etStartDate,
            R.id.etJobTerm, R.id.etCategories, R.id.etInr, R.id.imgAddAttachment})
    void handleOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.etRate:
                openAlertDialog(rates, getString(R.string.rate),
                        findIndexOfItem(rates, etRate.getText().toString()), new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(String item) {
                                etRate.setText(item);
                            }
                        });
                break;
            case R.id.etPaymentMethod:
                openAlertDialog(paymentMethods, getString(R.string.payment_method),
                        findIndexOfItem(paymentMethods, etPaymentMethod.getText().toString()),
                        new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(String item) {
                                etPaymentMethod.setText(item);
                            }
                        });
                break;
            case R.id.etPostLocation:
                startActivityForResult(new Intent(this, SearchLocationActivity.class), PICK_LOCATION);
                break;
            case R.id.etStartDate:
                openDatePickerDialog(etStartDate);
                break;
            case R.id.etJobTerm:
                openAlertDialog(jobTerms, getString(R.string.job_term),
                        findIndexOfItem(jobTerms, etJobTerm.getText().toString()), new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(String item) {
                                etJobTerm.setText(item);
                            }
                        });
                break;
            case R.id.etCategories:
                startActivityForResult(new Intent(this, CategoryActivity.class), PICK_CATEGORY);
                break;
            case R.id.etInr:

                break;
            case R.id.imgAddAttachment:
                openAttachmentDialog();
                break;
        }
    }

    @OnTextChanged(R.id.etPostTitle)
    void handlePostTitleChange(CharSequence s, int start, int before, int count) {
        setCounterText(txtPostTitleCounter, maxPostLength - s.length(), true);
    }

    @OnTextChanged(R.id.etPostDescription)
    void handlePostDescriptionChange(CharSequence s, int start, int before, int count) {
        setCounterText(txtPostDescCounter, maxPostDescLength - s.length(), true);
    }

    @OnFocusChange({R.id.etPostTitle, R.id.etPostDescription})
    void handleFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.etPostTitle:
                setCounterText(txtPostTitleCounter, maxPostLength - etPostTitle.length(), hasFocus);
                tilPostTitle.setHint(hasFocus || etPostTitle.length() != 0 ? getString(R.string.post_title) : getString(R.string.enter_post_title));
                break;
            case R.id.etPostDescription:
                setCounterText(txtPostDescCounter, maxPostDescLength - etPostDescription.length(), hasFocus);
                tilPostDescription.setHint(hasFocus || etPostDescription.length() != 0 ? getString(R.string.post_description) : getString(R.string.describe_your_post));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CATEGORY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<Category> selectedCategories = data.getParcelableArrayListExtra(PARAM_SELECTED_CATEGORY);

                if (selectedCategories != null) {
                    etCategories.setText(getString(R.string.no_of_selected_category, selectedCategories.size()));
                }
            }
        } else if (requestCode == PICK_LOCATION) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String location = data.getStringExtra(PARAM_SELECTED_LOCATION);
                etPostLocation.setText(location);
            }
        }
        else{
            MediaUtils.onActivityResult(this, requestCode, resultCode, data, new MediaUtils.OnMediaPicked() {
                @Override
                public void onSuccess(File file, Bitmap bm, MediaUtils.MediaType mediaType) {
                    PostMedia postMedia = new PostMedia();
                    postMedia.mediaType = mediaType;
                    postMedia.mediaPath = file.getAbsolutePath();
                    mPostMediaList.add(0, postMedia);
                    mAdapter.addData(postMedia);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_post, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isAllRequiredFieldFilled) {
            menu.getItem(0).setEnabled(true);
        } else {
            menu.getItem(0).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.actionPost) {

        }
        return false;
    }

    private void setCounterText(TextView txtView, int count, boolean hasFocus) {
        if (hasFocus && count != 0) {
            txtView.setText(getString(R.string.character_left, count));
            txtView.setVisibility(View.VISIBLE);
        } else {
            txtView.setVisibility(View.GONE);
        }
    }

    private void openDatePickerDialog(final EditText editText) {
        Calendar date;
        if (editText.getText().toString().trim().length() == 0) {
            date = Calendar.getInstance();
        } else {
            date = DateFormatUtils.getCalendarInstanceFromFormatedDate(DATE_RANGE_FORMAT,
                    editText.getText().toString().trim());
        }
        DatePickerDialog dpd = new DatePickerDialog(this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        editText.setText(DateFormatUtils.convertCalendarInstanceInRequestedDateFormat(newDate, DATE_RANGE_FORMAT));
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // do something for phones running an SDK before lollipop
                    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }
        };
        dpd.show();
    }

    private void openAlertDialog(final String[] data, String title, int index,
                                 final OnItemSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(data, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setTitle(title);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.select, null);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView lw = dialog.getListView();
                if (lw.getCheckedItemPosition() != -1) {
                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                    listener.onItemSelected((String) checkedItem);
                }
                dialog.dismiss();
            }
        });
    }

    private int findIndexOfItem(String[] data, String item) {
        return Arrays.asList(data).indexOf(item);
    }

    /**
     * will open bottom sheet dialog for attachment
     */
    private void openAttachmentDialog() {

        final BottomSheetDialog bsDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_attachment, null);
        bsDialog.setContentView(sheetView);
        bsDialog.show();

        TextView txtImageCapture = sheetView.findViewById(R.id.txtCaptureImage);
        TextView txtCaptureVideo = sheetView.findViewById(R.id.txtCaptureVideo);
        TextView txtPhotoVideo = sheetView.findViewById(R.id.txtPhotoVideo);
        TextView txtDocument = sheetView.findViewById(R.id.txtDocument);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.txtCaptureImage:
                        if (PermissionManager.checkWriteStoragePermission(NewPostActivity.this, 0)) {
                            MediaUtils.captureImage(NewPostActivity.this, MediaUtils.RequestCode.CAPTURE_IMAGE);
                        }
                        bsDialog.dismiss();
                        break;
                    case R.id.txtCaptureVideo:
                        if (PermissionManager.checkWriteStoragePermission(NewPostActivity.this, 0)) {
                            MediaUtils.captureVideo(NewPostActivity.this, MediaUtils.RequestCode.CAPTURE_VIDEO);
                        }
                        bsDialog.dismiss();
                        break;
                    case R.id.txtPhotoVideo:
                        if (PermissionManager.checkWriteStoragePermission(NewPostActivity.this, 0)) {
                            MediaUtils.selectImageVideo(NewPostActivity.this, MediaUtils.RequestCode.SELECT_IMAGE_VIDEO);
                        }
                        bsDialog.dismiss();
                        break;
                    case R.id.txtDocument:
                        MediaUtils.selectDocument(NewPostActivity.this, MediaUtils.RequestCode.SELECT_DOCUMENT);
                        bsDialog.dismiss();
                        break;
                }
            }
        };
        txtImageCapture.setOnClickListener(clickListener);
        txtCaptureVideo.setOnClickListener(clickListener);
        txtPhotoVideo.setOnClickListener(clickListener);
        txtDocument.setOnClickListener(clickListener);
    }
}
