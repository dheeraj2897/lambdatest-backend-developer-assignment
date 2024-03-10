# Contact Enrichment Deduplication

## Problem Statement

Contact enrichment involves fetching contact details and associated data of a person using their Name and Company (or a unique combination). Various data providers, such as Lusha, ZoomInfo, and Apollo, offer this functionality, producing JSON responses with attributes like firstName, email, phone, jobTitle, etc.

Your task is to create a single JSON file by combining data from different providers. This requires deduplication—matching attributes with different names (e.g., "jobTitle" and "job_title") and choosing/combining values.

## Solution Overview

The provided solution is a Spring Boot service with a CommandLineRunner that calls a service layer method. This method reads input JSON files, processes them, and outputs a final JSON with deduplicated attributes.

## Code Implementation

The solution is implemented in Java using the Spring Boot framework. The CommandLineRunner invokes the service method, which performs the following steps:

1. **Read JSON Files:** The service reads JSON files from the specified location.

2. **Deduplication Logic:** Deduplication involves matching attributes with different names and choosing the values.

3. **Combine Data:** The service combines data from different providers, resolving conflicts based on priority rankings or other criteria.

4. **Generate Final JSON:** The processed data is used to generate a final JSON file.

## Usage

To run the solution:

1. Clone the repository.
2. Build the project using Maven build tool.
3. Run the generated JAR file or deploy the service to a web server.

## Files Included

1. **Code Files (Java/Spring Boot):**
   - `JsonEnricherService.java`: Implements the deduplication and data combination logic.
   - `JsonEnricherApplication.java`: Executes the service method to process input JSON files once the spring application is started on port 8080.
   - `EnrichedData.java`: Model for combining the resultant json from the given input.
   - `application.properties`: Contains the application configuration like the port, input and output files location, etc.

2. **Final JSON Output:**
   - `enriched.json`: The resulting JSON file containing deduplicated data.

## Notes

- Ensure that the input JSON files are in the specified location.
