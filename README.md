[ESIEA UFA-3A]<br/>
ROUXEL & UL HASSAN SHAH


# Projet ImageIn

Cette application permet d'afficher une galerie d'image en récuperant une liste d'urls dans un JSON hébergé sur un serveur.
Les images peuvent être ensuite partagées ou téléchargées.
De nouvelles images pourront aussi être ajoutées au JSON par l'utilisateur.


## Attendus techniques

- [x] Langues EN et FR
- [x] Éléments graphiques de base
- [ ] Mode portrait/landscape pour une des activités
- [x] Au moins deux activités
- [ ] Notifications
    - [x] Toast
        - MainActivity - Téléchargement du JSON
        - ImageActivity - Permission d'accès au storage refusée
    - [ ] Notification dans la barre de notifications
    - [x] Boîte de dialogue
        - ImageActivity - Pendant le téléchargement d'une image
- [ ] Bouton dans l’action bar
- [ ] Service de téléchargement
- [ ] Notification de fin de téléchargement dans un BroadCastReceiver
- [x] Traitement des données téléchargées (JSON)
    - Requête HTTP pour récupérer ou mettre à jour un JSON sur un serveur distant (api : http://myjson.com/)
- [x] Appel vers une application externe
    - ImageActivity - Bouton share
- [x] Affichage des données téléchargées dans une liste
    - MainActivity - RecyclerView


## Bonus

- [ ] Enregistrement en base de données (SQLite)
- [ ] Sauvegarde de préférences utilisateur
- [ ] Lecture d'un capteur (GPS, accéléromètre)
- [ ] Onglets à base de fragments (difficile)