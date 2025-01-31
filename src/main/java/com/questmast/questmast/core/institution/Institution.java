package com.questmast.questmast.core.institution;

import com.questmast.questmast.core.person.JuridicalPerson;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Institution extends JuridicalPerson {
}
