<!--
SPDX-FileCopyrightText: 2021 Istituto Nazionale Previdenza Sociale

SPDX-License-Identifier: BSD-3-Clause
-->

# SPIDlibraryAndroid

SPIDlibraryAndroid is a library for logging in via SPID through several different identity providers.

## Requirements

- Android 4.4+

## Usage
1. Add dependency to your project:
```kotlin
repositories {
   maven {
       url = "https://maven.pkg.github.com/INPS-it/SPIDlibraryAndroid"
       credentials {
           username = GITHUB_USER
           password = GITHUB_TOKEN
       }
   }
}
dependencies {
    implementation("it.inps.spid:library:1.0.3")
}
```
2. Declare a variable to register the `IdentityProviderSelectorActivityContract` contract using the `registerForActivityResult(I)` method which will give a `SpidResult` object as a return value:
```kotlin
private val startSpidFlow = registerForActivityResult(IdentityProviderSelectorActivityContract()) { spidResult ->
    when (spidResult.spidEvent) {
        SpidEvent.GENERIC_ERROR -> { /* TODO */ }
        SpidEvent.NETWORK_ERROR -> { /* TODO */ }
        SpidEvent.SESSION_TIMEOUT -> { /* TODO */ }
        SpidEvent.SPID_CONFIG_ERROR -> { /* TODO */ }
        SpidEvent.SUCCESS -> { /* spidResult.spidReponse available */ }
        SpidEvent.USER_CANCELLED -> { /* TODO */ }
    }
}
```
The `SpidResult` object consists of a `SpidEvent` object and an optional `SpidResponse` object. The `SpidResponse` object is only available in case of successful login.

3. Create a `SpidParams.Config` object containing the `authPageUrl` url, the `callbackPageUrl` url and an optional timeout `int` value (default value: 30sec): 
```kotlin
val spidConfig = SpidParams.Config(
                    "https://<insert the auth url here>", // TODO
                    "https://<insert the callback url here>", // TODO
                    60
    )
```
4. Use the `IdentityProvider.Builder()` builder to add the identity providers:
```kotlin
val idpList = IdentityProvider.Builder()
                   .addAruba(idpParameter = "<insert the idp parameter here>")
                   .addPoste(idpParameter = "<insert the idp parameter here>")
                   .addTim(idpParameter = "<insert the idp parameter here>")
                   .addCustomIdentityProvider(
                           "CUSTOM IDENTITY PROVIDER",
                           R.drawable.ic_spid_idp_custom,
                           "<insert the idp parameter here>"
                   )
                   // TODO
                   .build()
```
5. Create a `SpidParams` object using the _spidConfig_ and _idpList_ objects and call the `ActivityResultLauncher.launch(I)` method:
```kotlin
startSpidFlow.launch(SpidParams(spidConfig, idpList))
```

## License

SPIDlibraryAndroid is released under the BSD 3-Clause License. [See LICENSE](https://github.com/INPS-it/SPIDlibraryAndroid/blob/main/LICENSE) for details.
