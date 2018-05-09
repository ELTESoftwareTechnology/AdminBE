package com.app.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationRequest implements Serializable {

    @NotNull
    private String targetEmail;

    private String targetName;
    private String comment;

}
