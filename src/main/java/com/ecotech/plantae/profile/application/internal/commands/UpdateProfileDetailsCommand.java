package com.ecotech.plantae.profile.application.internal.commands;

public record UpdateProfileDetailsCommand(String ownerId, String fullName, String timezone, String language) {}
