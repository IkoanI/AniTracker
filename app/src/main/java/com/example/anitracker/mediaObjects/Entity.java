package com.example.anitracker.mediaObjects;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Entity {
    protected Name name;
    protected String image, role, id, description, gender, bloodtype, age;
    protected Date dateOfbirth;
    protected int favorites;
    protected final Map<String, String> infoMap = new HashMap<>();

    abstract List<Info> getInfo();

    public Name getName() {
        return this.name;
    }

    public void setName(Name name) {
        this.name = name;
        if (StringUtils.isNotBlank(this.name.getFirst())) {
            infoMap.put("First Name", this.name.getFirst());
        }

        if (StringUtils.isNotBlank(this.name.getMiddle())) {
            infoMap.put("Middle Name", this.name.getMiddle());
        }

        if (StringUtils.isNotBlank(this.name.getLast())) {
            infoMap.put("Last Name", this.name.getLast());
        }

        if (StringUtils.isNotBlank(this.name.getFull())) {
            infoMap.put("Full Name", this.name.getFull());
        }

        if (StringUtils.isNotBlank(this.name.getNativeName())) {
            infoMap.put("Native Name", this.name.getNativeName());
        }

        if (this.name.getAlternatives() != null && !this.name.getAlternatives().isEmpty()) {
            infoMap.put("Alternative Names", String.join("\n\n", this.name.getNativeName()));
        }
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        if (StringUtils.isNotBlank(image)) {
            this.image = image;
        }
    }

    public Date getDateOfbirth() {
        return this.dateOfbirth;
    }

    public void setDateOfbirth(Date dateOfbirth) {
        this.dateOfbirth = dateOfbirth;
        if (!this.dateOfbirth.toString().isBlank()) {
            this.infoMap.put("Birthday", this.dateOfbirth.toString());
        }
    }

    public int getFavorites() {
        return this.favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
        this.infoMap.put("Favorites", String.valueOf(this.favorites));
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        if (StringUtils.isNotBlank(role)) {
            this.role = role;
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (StringUtils.isNotBlank(description)) {
            this.description = description;
        }
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        if (StringUtils.isNotBlank(gender)) {
            this.gender = gender;
            this.infoMap.put("Gender", this.gender);
        }
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        if (StringUtils.isNotBlank(age)) {
            this.age = age;
            this.infoMap.put("Age", this.age);
        }
    }

    public String getBloodtype() {
        return this.bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        if (StringUtils.isNotBlank(bloodtype)) {
            this.bloodtype = bloodtype;
            this.infoMap.put("Blood Type", this.bloodtype);
        }
    }
}
