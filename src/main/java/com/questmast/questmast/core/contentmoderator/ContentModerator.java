package com.questmast.questmast.core.contentmoderator;

import com.questmast.questmast.core.person.NaturalPerson;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ContentModerator extends NaturalPerson {
}
