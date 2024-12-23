package com.example.anitracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.activities.Details;
import com.example.anitracker.activities.EntityDetails;
import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.repository.AnilistObjectMappings;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.Image;
import com.example.anitracker.uiObjects.LanguageDropdown;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.List;
import java.util.Map;

public class CharacterViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Object> objectList;
    private final Context context;
    private final DetailsViewModel viewModel;
    private Map<String, StaffDetails> vnKnownVAs;

    private Boolean loading = false;

    private final int languageDropdownVar = 0,
            characterViewVar = 1;


    public CharacterViewAdapter(List<Object> objectList, Context context, DetailsViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
        this.objectList = objectList;
        if (viewModel.getType().equals(MediaType.VISUAL_NOVEL)) {
            this.vnKnownVAs = viewModel.getKnownVAs();
        }
    }

    public void addChars(List<CharacterDetails> newItems){
        this.loading = true;
        objectList.addAll(newItems);
        notifyItemRangeInserted(this.getItemCount()-newItems.size(), newItems.size());
        this.loading = false;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof LanguageDropdown) {
            return languageDropdownVar;
        } else if (objectList.get(position) instanceof CharacterDetails) {
            return characterViewVar;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case languageDropdownVar:
                view = inflater.inflate(R.layout.language_dropdown_layout, parent, false );
                return new CharacterViewAdapter.LanguageDropdownView(view);

            case characterViewVar:
                view = inflater.inflate(R.layout.character_card, parent, false );
                return new CharacterViewAdapter.CharacterView(view);

            default:
                //TODO make default view holder
                view = inflater.inflate(R.layout.character_card, parent, false );
                return new CharacterViewAdapter.CharacterView(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!loading && position >= getItemCount() - 1) {
            viewModel.getCharPage();
        }

        switch (holder.getItemViewType()) {
            case languageDropdownVar:
                LanguageDropdown languageDropdown = (LanguageDropdown) objectList.get(position);
                LanguageDropdownView languageDropdownView = (LanguageDropdownView) holder;

                if (languageDropdownView.languageDropdown.getAdapter() == null) {
                    languageDropdownView.languageDropdown.setAdapter(languageDropdown.getAdapter());
                }

                languageDropdownView.languageDropdown.setSelection(viewModel.getLastSelectedLanguagePos());

                languageDropdownView.languageDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (viewModel.getLastSelectedLanguagePos() != i) {
                            objectList.subList(1, objectList.size()).clear();
                            viewModel.setCurrCharPage(1);
                            viewModel.setLastSelectedLanguage(languageDropdown.getStaffLanguage(i));
                            viewModel.setLastSelectedLanguagePos(i);
                            viewModel.getCharPage();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // nothing to do
                    }
                });
                break;

            case characterViewVar:
                CharacterDetails character = (CharacterDetails) objectList.get(position);
                CharacterView characterView = (CharacterView) holder;

                if (this.vnKnownVAs != null && character.getVoiceActor() == null && vnKnownVAs.containsKey(character.getId())) {
                    StaffDetails va = vnKnownVAs.get(character.getId());
                    character.setVoiceActor(va);
                    assert va != null;
                    character.setNotes(va.getRole());
                }

                if (character.getNotes() != null) {
                    characterView.charName.setText(String.format("%s (%s)", character.getName().getUserPref(), character.getNotes()));
                } else {
                    characterView.charName.setText(character.getName().getUserPref());
                }
                characterView.role.setText(character.getRole());
                Image.loadImage(this.context, character.getImage(), characterView.charImage);

                characterView.charImage.setOnClickListener(e -> {
                    Intent intent = new Intent(context, EntityDetails.class);
                    intent.putExtra("ID", character.getId());
                    intent.putExtra("Type", viewModel.getType().rawValue);
                    context.startActivity(intent);
                });

                if (character.getVoiceActor() == null) {
                    characterView.hideVoiceActor();
                } else {
                    // recyclerview reuses view so if previously set gone, must be set visible again
                    characterView.showVoiceActor();
                    characterView.vaName.setText(character.getVoiceActor().getName().getUserPref());
                    Image.loadImage(this.context, character.getVoiceActor().getImage(), characterView.vaImage);
                    characterView.language.setText(AnilistObjectMappings.staffLanguageToString.get(viewModel.getLastSelectedLanguage()));
                }
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size() - (viewModel.getType().equals(MediaType.ANIME) ? 1 : 0);
    }

    private static class CharacterView extends RecyclerView.ViewHolder {
        public TextView charName, role, vaName, language;
        public ImageView charImage, vaImage;
        public CharacterView(@NonNull View itemView) {
            super(itemView);
            charName = itemView.findViewById(R.id.charName);
            role = itemView.findViewById(R.id.role);
            vaName = itemView.findViewById(R.id.vaName);
            language = itemView.findViewById(R.id.language);
            charImage = itemView.findViewById(R.id.charImage);
            vaImage = itemView.findViewById(R.id.vaImage);
        }

        public void hideVoiceActor() {
            this.vaImage.setVisibility(View.GONE);
            this.vaName.setVisibility(View.GONE);
            this.language.setVisibility(View.GONE);
        }

        public void showVoiceActor() {
            this.vaImage.setVisibility(View.VISIBLE);
            this.vaName.setVisibility(View.VISIBLE);
            this.language.setVisibility(View.VISIBLE);
        }

    }

    private static class LanguageDropdownView extends RecyclerView.ViewHolder{
        Spinner languageDropdown;
        public LanguageDropdownView(@NonNull View itemView) {
            super(itemView);
            languageDropdown = itemView.findViewById(R.id.languageDropdown);
        }
    }
}
