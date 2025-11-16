package com.ecotech.plantae.profile.application.commands;

public record UpdateProfileDetailsCommand(String ownerId, String fullName, String timezone, String language) {}
