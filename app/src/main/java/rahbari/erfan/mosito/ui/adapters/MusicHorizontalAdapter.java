package rahbari.erfan.mosito.ui.adapters;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;

import java.util.List;

import rahbari.erfan.mosito.R;
import rahbari.erfan.mosito.databinding.AdapterMusicHorizontalBinding;
import rahbari.erfan.mosito.models.Music;
import rahbari.erfan.mosito.utils.AdapterUtil;


public class MusicHorizontalAdapter extends AdapterUtil<Music, AdapterMusicHorizontalBinding> {
    public MusicHorizontalAdapter(Activity activity) {
        super(activity, R.layout.adapter_music_horizontal);
    }

    @Override
    public void bindViewHolder(AdapterMusicHorizontalBinding binding, Music item, int position) {
        binding.setMusic(item);
        binding.card.setOnClickListener(v -> play(item, position));
    }

    private void play(Music music, int position) {
        List<Music> next = getArray().subList(position, Math.min(getItemCount(), 50));
        Intent i = new Intent("PLAYER");
        i.putExtra("action", "play");
        i.putExtra("music", music);
        i.putExtra("next", new Gson().toJson(next));
        activity.sendBroadcast(i);
    }
}
