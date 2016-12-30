[ESIEA UFA-3A]<br/>
ROUXEL & UL HASSAN SHAH


# Projet ImageIn

Cette application permet d'afficher une galerie d'image en récuperant une liste d'urls dans un JSON hébergé sur un serveur.
Les images peuvent être ensuite partagées ou téléchargées.
De nouvelles images pourront aussi être ajoutées au JSON par l'utilisateur.


## Attendus techniques

- [x] Langues EN et FR
- [x] Éléments graphiques de base
- [x] Mode portrait/landscape pour une des activités
    - MainActivity - Le nombre de colonnes change en fonction de l'orientation
- [x] Au moins deux activités
- [x] Notifications
    - [x] Toast
        - MainActivity - Téléchargement du JSON
        - ImageActivity - Permission d'accès au storage refusée
    - [x] Notification dans la barre de notifications
        - ImageActivity - Notification de progression du téléchargement de l'image
    - [x] Boîte de dialogue
        - SettingsActivity - Préférence du nombre de rangées
- [x] Bouton dans l’action bar
    - MainActivity - Bouton pour les options
    - SettingsActivity - Bouton retour précédente activité
- [x] Service de téléchargement
    - ImageService
- [x] Notification de fin de téléchargement dans un BroadCastReceiver
    - MainActivity - Reçoit la notification de fin de téléchargement du json dans un BroadcastReceiver
- [x] Traitement des données téléchargées (JSON)
    - Requête HTTP pour récupérer ou mettre à jour un JSON sur un serveur distant (api : http://myjson.com/)
- [x] Appel vers une application externe
    - ImageActivity - Bouton share
    - ImageActivity - Clic sur notification de fin de téléchargement de l'image envoie vers la galerie
- [x] Affichage des données téléchargées dans une liste
    - MainActivity - RecyclerView


## Bonus

- [x] Enregistrement en base de données (SQLite)
    - UploadActivity - ExtrasFragment - Secouez pour sauvegarder la date courant dans la base
- [x] Sauvegarde de préférences utilisateur
    - SettingsActivity - Sauvegarde de la préférence du nombre de rangées en portrait/landscape dans le MainActivity
- [x] Lecture d'un capteur (GPS, accéléromètre)
    - UploadActivity - ExtrasFragment - Secouez pour sauvegarder la date courant dans la base
- [x] Onglets à base de fragments (difficile)
    - UploadActivity - UploadFragment & ExtrasFragment


## Autres fonctionnalités implémentées (pas dans le sujet)
- MainActivity - SwipeContainer (pull to refresh)
- MainActivity - BroadcastReceiver pour la modifications des préférences
- MainActivity - BroadcastReceiver pour l'upload d'images
- SettingsActivity - Flèche retour arrière dans la toolbar
- Splash screen au lancement de l'application
- ImageActivity - Clic rapide sur le fond cache les FloatingActionButtons
- ImageActivity - Image mise dans une WebView, permettant aussi de zoomer
- UploadActivity - Modification du JSON (api : http://myjson.com/)


## Tests

- Smartphone sous Marshmallow (API 23)
- Ėmulateur sous KitKat (API 19)

Aucun problème fonctionnel observé. L'interface peut varier légèrement en dessous de Lollipop.
Comportement en dessous de KitKat inconnu.


## Remarques

L'upload d'images n'a pas été sécurisé par soucis de priorités des tâches pour le projet. Toute chaine de caractères peut y être placée.
Des comportements inattendus peuvent donc survenir si la fonction n'est pas utilisée correctement (ajout de l'url d'une image).
Si un lien est incorrect ou qu'il n'y a pas de connexion, le logo de l'application doit s'afficher.<br/>
De plus, l'UploadActivity regroupe quelques fonctionnalités incohérentes afin de satisfaire certains bonus. Veuillez nous pardonner m(_ _)m.
