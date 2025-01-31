package com.questmast.questmast.core.student;

import com.questmast.questmast.core.person.NaturalPerson;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class student extends NaturalPerson {
}
