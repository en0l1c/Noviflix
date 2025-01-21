# Noviflix Android App

## Description
Noviflix is a modern application designed to provide movie info from a specific API.

## Development Details
The application was developed and tested on a **Google Pixel 8a** to ensure compatibility and smooth performance on modern devices.

## Setup Instructions

1. **Creating the `network_security_config.xml` File**  
   You need to create a file named `network_security_config.xml` in the following directory:  
   `src/debug/res/xml/`

   This file is required to configure connections for the app if the url is type of HTTP. Ensure that the file includes the necessary domains for your application's functionality.

   ### Example Configuration
   ```xml
   <network-security-config>
       <domain-config cleartextTrafficPermitted="true">
           <domain includeSubdomains="true">[your_domain]</domain>
       </domain-config>
   </network-security-config>

2. **Setting the System Variable `DEBUG_BASE_URL`**
To ensure the `build.gradle` configuration works correctly, you must add the following environment variable to your system:

- **Variable Name:** `DEBUG_BASE_URL`  
- **Value:** `[your_debug_api_base_url]`

Make sure this variable points to the correct base URL for debugging purposes.


# Screenshots

Here are some screenshots of the application:

<p align="center">
  <img src="screenshots/Screenshot_20250120-174708.png" alt="Screenshot 1" width="200"/>
  <img src="screenshots/Screenshot_20250120-174721.png" alt="Screenshot 2" width="200"/>
  <img src="screenshots/Screenshot_20250120-174732.png" alt="Screenshot 3" width="200"/>
  <img src="screenshots/Screenshot_20250120-174741.png" alt="Screenshot 4" width="200"/>
  <img src="screenshots/Screenshot_20250120-174816.png" alt="Screenshot 5" width="200"/>
  <img src="screenshots/Screenshot_20250120-174836.png" alt="Screenshot 6" width="200"/>
  <img src="screenshots/Screenshot_20250120-174849.png" alt="Screenshot 7" width="200"/>
  <img src="screenshots/Screenshot_20250120-174906.png" alt="Screenshot 8" width="200"/>
  <img src="screenshots/Screenshot_20250120-174914.png" alt="Screenshot 9" width="200"/>
  <img src="screenshots/Screenshot_20250120-174921.png" alt="Screenshot 10" width="200"/>
  <img src="screenshots/Screenshot_20250120-174926.png" alt="Screenshot 11" width="200"/>
  <img src="screenshots/Screenshot_20250120-174943.png" alt="Screenshot 12" width="200"/>
  <img src="screenshots/Screenshot_20250120-174956.png" alt="Screenshot 13" width="200"/>
  <img src="screenshots/Screenshot_20250120-175000.png" alt="Screenshot 14" width="200"/>
  <img src="screenshots/Screenshot_20250120-175011.png" alt="Screenshot 15" width="200"/>
</p>
