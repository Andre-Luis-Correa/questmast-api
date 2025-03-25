package com.questmast.questmast.core.student.domain.model;

import com.questmast.questmast.core.person.NaturalPerson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Student extends NaturalPerson {

}
