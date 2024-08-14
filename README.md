-> Project Scope:
   * Developing an e-commerce web application with RESTful web services using Spring boot. The application encompasses various        features and services, including product catalog, shopping cart, checkout, etc. 
   * Implementing user authentication and authorization protocols using Spring Security, Open Authorization (OAuth) and JSON web 
     token (JWT) for secure access.
   * Implementing a DevOps pipeline facilitating CI/CD using Jenkins and Docker for automated testing, creation of container    
     images, and vulnerability scanning. Adopting GitOps for automated application deployment to Kubernetes using ArgoCD.

   * Git repo used: GitHub (for source code, Jenkinsfile, Dockerfile, and K8s manifest files(Helm chart))
  
-> Creation of Jenkins Pipeline:
   * Start Jenkins
   * Install Pipeline plugin
   * Create a new Jenkins Job
   * Create pipeline script / Jenkinsfile, (pipeline-as-a-code) using Groovy Sandbox
   * Build and test the pipeline
     
-> How to get JenkinsFile from Git SCM:
   * Create a new job or use existing job (type: Pipeline)
   * Create a repo on Github
   * Add JenkinsFile in the repo
   * Select Jenkins job > Pipeline section > Select Definition Pipeline script from SCM
   * Add repo and JenskinsFile location in the job under Pipeline section
   * Save and run
     
