package rahbari.erfan.mosito.ui.playlists;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import rahbari.erfan.mosito.Application;
import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.FragmentCreatePlaylistBinding;
import rahbari.erfan.mosito.interfaces.Response;
import rahbari.erfan.mosito.models.ErrorHandler;
import rahbari.erfan.mosito.models.Playlist;
import rahbari.erfan.mosito.resources.Repositories.PlaylistRep;
import rahbari.erfan.mosito.resources.Repositories.UserRep;
import rahbari.erfan.mosito.resources.models.UploadRequest;
import rahbari.erfan.mosito.utils.FragmentUtil;
import rahbari.erfan.mosito.utils.Utils;

public class PlaylistCreateFragment extends FragmentUtil<FragmentCreatePlaylistBinding> implements UploadRequest.UploadCallbacks, Response<Playlist> {
    public static final int PICK_IMAGE = 101;

    private PlaylistRep rep;
    private UserRep userRep;
    private File picture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rep = new PlaylistRep();
        userRep = new UserRep();
    }

    @Override
    public void onViewCreate() {

        userRep.userLiveData().observe(this, user -> binding.setUserErrors(user == null));

        binding.btnPicture.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        binding.btnCancel.setOnClickListener(view -> new MaterialAlertDialogBuilder(activity)
                .setTitle(getString(R.string.discard_playlist))
                .setPositiveButton(getString(R.string.cancel), null)
                .setNegativeButton(getString(R.string.yes), (dialogInterface, i) -> navigation.closeFragment(this))
                .show());

        binding.btnCreate.setOnClickListener(view -> {
            clearErrors();
            rep.create(
                    Utils.text(binding.edtName),
                    binding.switchType.isChecked() ? "public" : "private",
                    Utils.text(binding.edtDescription),
                    picture,
                    this, this, this::response);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_playlist;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            try {
                File outputFile = File.createTempFile("tmp", null, activity.getCacheDir());

                UCrop.of(data.getData(), Uri.fromFile(outputFile))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(1024, 1024)
                        .start(activity.getApplicationContext(), this);

                outputFile.deleteOnExit();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            picture = new File(resultUri.getPath());
            binding.imgPicture.setImageURI(resultUri);
            binding.imgPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(activity, cropError.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        binding.setPercent(percentage + "%");
    }

    public void response(ErrorHandler handler) {
        activity.runOnUiThread(() -> {
            binding.setPercent(null);
            Utils.errorHandler(binding.edtName, handler, "title");
            Utils.errorHandler(binding.edtDescription, handler, "description");

            if (handler != null && handler.getErrors() != null && handler.getErrors().containsKey("picture")) {
                List<String> errors = handler.getErrors().get("picture");
                binding.setPictureErrors(errors.get(0));
            }

            if (handler != null && handler.getErrors() != null && handler.getErrors().containsKey("type")) {
                List<String> errors = handler.getErrors().get("type");
                binding.setTypeErrors(errors.get(0));
            }
        });
    }

    @Override
    public void response(Playlist response) {
        activity.runOnUiThread(() -> {
            binding.setPercent(null);
            Intent i = new Intent("TOAST_DIALOG");
            i.putExtra("message", Application.getInstance().getString(R.string.playlist_created));
            Application.getInstance().sendBroadcast(i);
            navigation.replaceFragment(this, PlaylistFragment.newInstance(response));
        });
    }

    private void clearErrors() {
        binding.setPictureErrors(null);
        binding.setTypeErrors(null);
        binding.edtName.setError(null);
        binding.edtDescription.setError(null);
    }
}
