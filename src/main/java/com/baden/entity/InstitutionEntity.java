package com.baden.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "institution")
@Data
public class InstitutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer institution_id;

    @Column(name = "institution_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String institution_name;

    @Column(name = "institution_description", columnDefinition = "TEXT")
    private String institution_description;

    @Column(name = "institution_email", columnDefinition = "VARCHAR(50)", nullable = false)
    private String institution_email;

    @Column(name = "institution_phone", columnDefinition = "VARCHAR(20)")
    private String institution_phone;

    @Column(name = "institution_website", columnDefinition = "VARCHAR(50)", nullable = false)
    private String institution_website;

    public InstitutionEntity() {

    }

    //This constructor is only meant for testing purposes.
    public InstitutionEntity(Integer institution_id, String institution_name, String institution_description, String institution_email, String institution_phone, String institution_website) {
        this.institution_id = institution_id;
        this.institution_name = institution_name;
        this.institution_description = institution_description;
        this.institution_email = institution_email;
        this.institution_phone = institution_phone;
        this.institution_website = institution_website;
    }

    public InstitutionEntity(String institution_name, String institution_description, String institution_email, String institution_phone, String institution_website) {
        this.institution_name = institution_name;
        this.institution_description = institution_description;
        this.institution_email = institution_email;
        this.institution_phone = institution_phone;
        this.institution_website = institution_website;
    }

}
