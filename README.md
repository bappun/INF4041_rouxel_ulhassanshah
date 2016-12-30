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
    - GetImageService
- [x] Notification de fin de téléchargement dans un BroadCastReceiver
    - MainActivity - Reçoit la notification de fin de téléchargement du json dans un BroadcastReceiver
- [x] Traitement des données téléchargées (JSON)
    - Requête HTTP pour récupérer ou mettre à jour un JSON sur un serveur distant (api : http://myjson.com/)
- [x] Appel vers une application externe
    - ImageActivity - Bouton share
    - ImageActivity - CLic sur notification de fin de téléchargement de l'image envoie vers la galerie
- [x] Affichage des données téléchargées dans une liste
    - MainActivity - RecyclerView


## Bonus

- [ ] Enregistrement en base de données (SQLite)
- [x] Sauvegarde de préférences utilisateur
    - SettingsActivity - Sauvegarde de la préférence du nombre de rangées portrait/landscape
- [ ] Lecture d'un capteur (GPS, accéléromètre)
- [ ] Onglets à base de fragments (difficile)