package com.example.anitracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.activities.Details;
import com.example.anitracker.activities.EntityDetails;
import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.repository.AnilistObjectMappings;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.Image;
import com.example.anitracker.uiObjects.LanguageDropdown;
import com.example.anitracker.viewModels.DetailsViewModel;
import com.example.anitracker.viewModels.EntityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CharacterViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Object> objectList;
    private final Context context;

    private DetailsViewModel detailsViewModel;
    private EntityViewModel entityViewModel;

    private Map<String, StaffDetails> vnKnownVAs;

    private boolean loading = false;

    private final int languageDropdownVar = 0,
            characterViewVar = 1;


    public CharacterViewAdapter(Context context, ViewModel viewModel) {
        this.context = context;
        Log.d("TESTING", String.valueOf(viewModel.getClass()));
        if (viewModel instanceof DetailsViewModel) {
            this.detailsViewModel = (DetailsViewModel) viewModel;
        } else if (viewModel instanceof EntityViewModel) {
            this.entityViewModel = (EntityViewModel) viewModel;
        }
        this.objectList = new ArrayList<>();
        if (this.detailsViewModel != null && this.detailsViewModel.getType().equals(MediaType.VISUAL_NOVEL)) {
            this.vnKnownVAs = detailsViewModel.getKnownVAs();
        }
    }

    public void addObjects(List<?> objects) {
        this.loading = true;
        objectList.addAll(objects);
        notifyItemRangeInserted(this.getItemCount()-objects.size(), objects.size());
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
        if (!this.loading && position >= getItemCount() - 1) {
            if (detailsViewModel != null) {
                detailsViewModel.getCharPage();
            } else if (entityViewModel != null) {
                entityViewModel.getStaffChars();
            }
        }

        switch (holder.getItemViewType()) {
            case languageDropdownVar:
                LanguageDropdown languageDropdown = (LanguageDropdown) objectList.get(position);
                LanguageDropdownView languageDropdownView = (LanguageDropdownView) holder;

                if (languageDropdownView.languageDropdown.getAdapter() == null) {
                    languageDropdownView.languageDropdown.setAdapter(languageDropdown.getAdapter());
                }

                languageDropdownView.languageDropdown.setSelection(detailsViewModel.getLastSelectedLanguagePos());

                languageDropdownView.languageDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (detailsViewModel.getLastSelectedLanguagePos() != i) {
                            objectList.subList(1, objectList.size()).clear();
                            detailsViewModel.setCurrCharPage(1);
                            detailsViewModel.setLastSelectedLanguage(languageDropdown.getStaffLanguage(i));
                            detailsViewModel.setLastSelectedLanguagePos(i);
                            detailsViewModel.getCharPage();
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
                    if (detailsViewModel != null) {
                        intent.putExtra("Type", detailsViewModel.getType().rawValue);
                    } else {
                        intent.putExtra("Type", entityViewModel.getMediaType().rawValue);
                    }
                    intent.putExtra("Entity", "Char");
                    context.startActivity(intent);
                });

                if (character.getCharMedia() != null) {
                    characterView.showRelation();
                    MediaDetails charMedia = character.getCharMedia();
                    characterView.relationName.setText(charMedia.getTitles().getUserPref());
                    Image.loadImage(this.context, charMedia.getImage(), characterView.relationImage);
                    characterView.relationImage.setOnClickListener(e -> {
                        Intent intent = new Intent(context, Details.class);
                        intent.putExtra("ID", charMedia.getId());
                        intent.putExtra("Type", charMedia.getType().rawValue);
                        context.startActivity(intent);
                    });
                    characterView.relationInfo.setText(String.format("%s · %s", charMedia.getFormat(), AnilistObjectMappings.mediaStatusToString.get(charMedia.getStatus())));

                } else if (character.getVoiceActor() != null) {
                    // recyclerview reuses view so if previously set gone, must be set visible again
                    characterView.showRelation();
                    StaffDetails va = character.getVoiceActor();
                    characterView.relationName.setText(va.getName().getUserPref());
                    Image.loadImage(this.context, va.getImage(), characterView.relationImage);
                    characterView.relationImage.setOnClickListener(e -> {
                        Intent intent = new Intent(context, EntityDetails.class);
                        intent.putExtra("ID", va.getId());
                        if (detailsViewModel != null) {
                            intent.putExtra("Type", detailsViewModel.getType().rawValue);
                        } else {
                            intent.putExtra("Type", entityViewModel.getMediaType().rawValue);
                        }
                        intent.putExtra("Entity", "Staff");
                        context.startActivity(intent);
                    });

                    if (detailsViewModel != null) {
                        characterView.relationInfo.setText(AnilistObjectMappings.staffLanguageToString.get(detailsViewModel.getLastSelectedLanguage()));
                    }
                } else {
                    characterView.hideRelation();
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        if (detailsViewModel != null) {
            return Math.max(0, objectList.size() - (detailsViewModel.getType().equals(MediaType.ANIME) ? 1 : 0));
        }

        return objectList.size();
    }

    private static class CharacterView extends RecyclerView.ViewHolder {
        public TextView charName, role, relationName, relationInfo;
        public ImageView charImage, relationImage;
        public CharacterView(@NonNull View itemView) {
            super(itemView);
            charName = itemView.findViewById(R.id.charName);
            role = itemView.findViewById(R.id.role);
            relationName = itemView.findViewById(R.id.relationName);
            relationInfo = itemView.findViewById(R.id.relationInfo);
            charImage = itemView.findViewById(R.id.charImage);
            relationImage = itemView.findViewById(R.id.relationImage);
        }

        public void hideRelation() {
            this.relationImage.setVisibility(View.GONE);
            this.relationName.setVisibility(View.GONE);
            this.relationInfo.setVisibility(View.GONE);
        }

        public void showRelation() {
            this.relationImage.setVisibility(View.VISIBLE);
            this.relationName.setVisibility(View.VISIBLE);
            this.relationInfo.setVisibility(View.VISIBLE);
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
