package com.example.anitracker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anitracker.R;
import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.repository.AnilistObjectMappings;
import com.example.anitracker.type.StaffLanguage;
import com.example.anitracker.uiObjects.LanguageDropdown;
import com.example.anitracker.viewModels.DetailsViewModel;

import java.util.List;
import java.util.Objects;

public class CharacterViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Object> objectList;
    private final Context context;
    private final DetailsViewModel viewModel;
    private Boolean loading = false, hasMorePages = true;
    private final int languageDropdownVar = 0, characterViewVar = 1;
    private StaffLanguage selectedLanguage =  StaffLanguage.JAPANESE;
    private final ArrayAdapter<String> languageDropdownAdapter;
    private int previousLanguagePosition = 0, currRole = 0;

    public CharacterViewAdapter(List<Object> objectList, Context context, DetailsViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
        this.objectList = objectList;
        if(objectList.isEmpty()){
            if(Objects.equals(this.viewModel.getType(), "Visual Novel")){
                this.viewModel.getVNCharPage();
            }
            else{
                this.viewModel.getCharPage(selectedLanguage);
            }
        }

        String[] languages = {"Japanese", "English", "Korean", "Italian", "Spanish", "Portuguese", "French", "German", "Hebrew", "Hungarian"};
        languageDropdownAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, languages);
    }

    public void addChars(List<CharacterDetails> newItems){
        loading = true;
        objectList.addAll(newItems);
        notifyItemRangeInserted(objectList.size()-newItems.size(), newItems.size());
        loading = false;
    }

    @Override
    public int getItemViewType(int position) {
        if(objectList.get(position) instanceof LanguageDropdown){
            return languageDropdownVar;
        }
        else if(objectList.get(position) instanceof CharacterDetails){
            return characterViewVar;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case languageDropdownVar:
                view = inflater.inflate(R.layout.language_dropdown_layout, parent, false );
                viewHolder = new CharacterViewAdapter.LanguageDropdownView(view);
                break;

            case characterViewVar:
                view = inflater.inflate(R.layout.character_card, parent, false );
                viewHolder = new CharacterViewAdapter.CharacterView(view);
                break;
        }

        assert viewHolder != null;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!loading && position >= getItemCount()-1){
            if(Objects.equals(viewModel.getType(), "Visual Novel") && hasMorePages) {
                viewModel.getVNCharPage();
            }
            else if(!Objects.equals(viewModel.getType(), "Visual Novel")){
                viewModel.getCharPage(selectedLanguage);
            }
        }

        switch (holder.getItemViewType()){
            case languageDropdownVar:
                LanguageDropdown languageDropdown = (LanguageDropdown) objectList.get(position);
                LanguageDropdownView languageDropdownView = (LanguageDropdownView) holder;
                languageDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if(languageDropdownView.languageDropdown.getAdapter() == null){
                    languageDropdownView.languageDropdown.setAdapter(languageDropdownAdapter);
                }
                languageDropdownView.languageDropdown.setSelection(previousLanguagePosition);

                languageDropdownView.languageDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedLanguage = languageDropdown.getStaffLanguages()[i];
                        if(previousLanguagePosition != i){
                            objectList.subList(1, objectList.size()).clear();
                            viewModel.setCurrCharPage(1);
                            viewModel.getCharPage(selectedLanguage);
                            previousLanguagePosition = i;
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

                if(character.getNotes() != null){
                    characterView.charName.setText(String.format("%s (%s)", character.getName().getUserPref(), character.getNotes()));
                }
                else {
                    characterView.charName.setText(character.getName().getUserPref());
                }

                characterView.role.setText(character.getRole());
                Glide.with(context.getApplicationContext()).load(character.getImage()).into(characterView.charImage);
                if(character.getVoiceActor() == null){
                    characterView.vaImage.setVisibility(View.GONE);
                    characterView.vaName.setVisibility(View.GONE);
                    characterView.language.setVisibility(View.GONE);
                }
                else{
                    // recyclerview reuses view so if previously set gone, must be set visible again
                    characterView.vaImage.setVisibility(View.VISIBLE);
                    characterView.vaName.setVisibility(View.VISIBLE);
                    characterView.language.setVisibility(View.VISIBLE);
                    characterView.vaName.setText(character.getVoiceActor().getName().getUserPref());
                    Glide.with(context.getApplicationContext()).load(character.getVoiceActor().getImage()).into(characterView.vaImage);
                    if(Objects.equals(viewModel.getType(), "Visual Novel")){
                        characterView.language.setText(character.getVoiceActor().getLang());
                    }
                    else{
                        characterView.language.setText(AnilistObjectMappings.staffLanguageToString.get(selectedLanguage));
                    }
                }
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public void setHasMorePages(Boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public static class CharacterView extends RecyclerView.ViewHolder{
        TextView charName, role, vaName, language;
        ImageView charImage, vaImage;
        public CharacterView(@NonNull View itemView) {
            super(itemView);
            charName = itemView.findViewById(R.id.charName);
            role = itemView.findViewById(R.id.role);
            vaName = itemView.findViewById(R.id.vaName);
            language = itemView.findViewById(R.id.language);
            charImage = itemView.findViewById(R.id.charImage);
            vaImage = itemView.findViewById(R.id.vaImage);
        }
    }

    public static class LanguageDropdownView extends RecyclerView.ViewHolder{
        Spinner languageDropdown;
        public LanguageDropdownView(@NonNull View itemView) {
            super(itemView);
            languageDropdown = itemView.findViewById(R.id.languageDropdown);
        }
    }
}
