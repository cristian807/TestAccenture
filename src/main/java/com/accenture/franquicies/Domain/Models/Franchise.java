package com.accenture.franquicies.Domain.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Franchise {
    private final Long id;
    private String name;
    @Builder.Default
    private List<Branch> branches = new ArrayList<>();

    public void updateName(String name) {
        this.name = name;
    }

    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }
}
