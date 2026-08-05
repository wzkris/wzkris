package com.wzkris.auth.remote.interfaces.admin.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPermsQueryRequest implements Serializable {

    @Nonnull
    private Long id;

    @Nullable
    private Long deptId;

}

