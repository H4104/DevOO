var ctrl = new Controleur();

/////////////////////////////////////////////////////////////////////////
/*
if (window.XMLHttpRequest)
{// code for IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp=new XMLHttpRequest();
}
else
{// code for IE6, IE5
    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
}
xmlhttp.open("GET","xml/plan10x10.xml",false);
xmlhttp.send();
xmlDoc=xmlhttp.responseXML;

console.log(xmlDoc);
*/
var map = ctrl.vue.map;
/*
var nodes = xmlDoc.getElementsByTagName("Noeud");
for( var i = 0; i < nodes.length; ++i){
    var node = nodes[i];
    var pos = [node.getAttribute('x')/1000, node.getAttribute('y')/1000];

    var trs = node.getElementsByTagName("LeTronconSortant");
    for( var j = 0; j < trs.length; ++j){
        var tr = trs[j];
        var node2 = xmlDoc.getElementById(tr.getAttribute("idNoeudDestination"));
        var pos2 = [node2.getAttribute('x')/1000, node2.getAttribute('y')/1000];

        addLine(pos, pos2, tr.getAttribute("nomRue"));
    }
}

for( var i = 0; i < nodes.length; ++i){
    var node = nodes[i];
    var pos = [node.getAttribute('x')/1000, node.getAttribute('y')/1000];
    addCircle(pos,node.getAttribute('id'));
}

function addLine( A, B, label){
    L.polyline([A,B],{weight:5,color:'#fff',opacity:0.8})
        .addTo(map)
        .bindLabel(label)
        .on("mouseover", function (){
            this.setStyle({color:"#0f0"});
        })
        .on("mouseout", function (){
            this.setStyle({color:'#fff'});
        });
}

function addCircle( pos, id){
    var circle = L.circle(pos, 520, {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.5
    }).addTo(map)
        .bindPopup("Node "+id+"<br>("+pos[0]+","+pos[1]+")", {offset: L.point(0,-10),closeButton:false})
        .on("mouseover",function () {this.openPopup();})
        .on("mouseout",function () {this.closePopup();})
        .on("click",function () {alert("Vous avez sélectionné un noeud. Malheureusement cette fonctionnalité n'est pas encore implémentée...");});
};
*/
//////////////////////////////////////////////////////////////////////////////////
if (window.File && window.FileReader && window.FileList && window.Blob) {
    // Great success! All the File APIs are supported.
} else {
    alert('Les APIs pour l\'ouverture des fichiers ne sont pas pris en charge');
}


///////////////////////////////////////////////////
// Class Controleur

function Controleur(){

    // attributes
    var com = new Com();
    var vue;

    // functions
    this.annuler = function() {
        console.log("undo");
    };
    this.retablir = function(){
        console.log("redo");
    };
    this.clicChargerPlan = function(){
        document.getElementById('charger-plan').click();
        //... récupérer fichier
        //c.appelService("test", [50,20], function(reponse){alert(reponse);});
    };
    this._chargerPlanOk = function(msg){
        vue.info(msg);
        // vue.creerPlan();
    }.bind(this);
    this.chargerPlan = function(evt){
        com.envoyerXml(evt,'charger-plan',this._chargerPlanOk);
    }.bind(this);

    // déclenche le clic sur l'élément 'input' de la page html
    this.clicChargerLivraisons = function(){
        document.getElementById('charger-livraisons').click();
    };
    // Handler pour le retour du service charger-livraisons
    this._chargerLivraisonsOk = function(msg){
        vue.info(msg);
        // vue.creerPlan();
    };
    this.chargerLivraisons = function(evt){
        com.envoyerXml(evt,'charger-livraisons',this._chargerLivraisonsOk);
    }.bind(this);

    this.clicTelechargerInitineraire = function(){
        var pdf = new jsPDF();
        var elementHandler = {
            '#ignorePDF': function (element, renderer) {
                return true;
            }
        };
        var source = window.document.getElementById('FDR');
        pdf.fromHTML(
            source,
            15,
            15,
            {
              'width': 180,'elementHandlers': elementHandler
            });

        pdf.output("dataurlnewwindow");
        //... récupérer fichier
    };

    // init
    this.vue = vue = new Vue(this);

}

///////////////////////////////////////////////////
// Class Vue

function Vue(controleur){
    // functions
    this.enableRedo = function(){
        console.log("Vue.enableRedo");
    };
    this.disableRedo = function(){
        console.log("Vue.disableRedo");
    };
    this.erreur = function(msg){
        alert(msg);
    };
    this.info = function(msg){
        alert(msg);
        /*var popup = L.popup()
        .setLatLng([0.4, 0.4])
        .setContent(msg)
        .openOn(map);*/
    };

    //Constructeur
    // initialisation de la map
    this.map = L.map('map',{maxBounds:[[-0.1,-0.1],[0.9,0.9]],zoomControl:false}).setView([0.4, 0.4], 10);
    // Ajout des controls (boutons)
    console.log("ctrl : ");
    console.log(controleur);
    L.control.zoom({position:'topright'}).addTo(this.map);
    L.easyButton('fa-arrow-circle-left', controleur.annuler, 'Undo', this.map);
    L.easyButton('fa-arrow-circle-right', controleur.retablir, 'Redo', this.map);
    L.easyButton('fa-road', controleur.clicChargerPlan, 'Charger un plan', this.map).setPosition('bottomleft');
    document.getElementById('charger-plan').addEventListener('change', controleur.chargerPlan, false);
    L.easyButton('fa-cubes', controleur.clicChargerLivraisons, 'Charger les livraison', this.map).setPosition('bottomleft');
    L.easyButton('fa-plus', null, 'Ajouter une livraison', this.map).setPosition('bottomleft');
    this.controlCalcul = L.easyButton('fa-refresh', null, "Calculer l'itinéraire", this.map).setPosition('bottomleft');
    this.controlCalcul.getContainer().getElementsByTagName('i')[0].className += " fa-spin";
    console.log(this.controlCalcul.getContainer().className);
    var controlFDR = L.easyButton('fa-file-text', controleur.clicTelechargerInitineraire, "Télécharger la feuille de route", this.map).setPosition('bottomright');

}

function VueIntersection(pos, id){

    // attributs
    this.pos = pos;
    this.id = id;

    this.paramDefaut = {color: '#fff', fillColor: '#fff', fillOpacity: 0.5};
    this.rayonDefaut = 520;

    this.cercle;

    // methodes
    this.afficher = function(){
        this.cercle.addTo(map);
        return this;
    }

    this.masquer = function(){
        map.removeLayer(this.cercle);
        return this;
    }

    this._clic = function(e){
        //todo : Clic sur une intersection
        console.log(this);
    }

    // Constructeur
    this.cercle = L.circle(pos, this.rayonDefaut, this.paramDefaut)
            .bindPopup("Noeud "+id+"<br>("+pos[0]+","+pos[1]+")", {offset: L.point(0,-10),closeButton:false})
            .on("mouseover",function () {this.openPopup();})
            .on("mouseout",function () {this.closePopup();})
            .on("click",this._clic, this); // 3e parametre -> pour changer le this

    return this;
}

function VueRoute(intersec1, intersec2, nom){
    // attributs
    this.A = intersec1;
    this.B = intersec2;
    this.nom = nom;

    this.paramDefaut = {weight:5,color:'#fff',opacity:0.8};
    
    this.ligne;

    // methodes
    this.afficher = function(){
         this.ligne.addTo(map);
         return this;
    }

    this.masquer = function(){
        map.removeLayer(this.ligne);
        this.A.masquer();
        this.B.masquer();
        return this;
    }

    this._clic = function(evt){
        this.masquer();
    }

    // Constructeur
    console.log(this);
    this.ligne = L.polyline([this.A.pos,this.B.pos],this.paramDefaut)
        .bindLabel(this.nom)
        .on("mouseover", function (){
            this.setStyle({color:"#0f0"});
        })
        .on("mouseout", function (){
            this.setStyle({color:'#fff'});
        })
        .on('click', this._clic, this);

    return this;
}

var v1 = new VueIntersection([0.4,0.4],2).afficher();
var v2 = new VueIntersection([0.6,0.4],2).afficher();
var r = new VueRoute(v1,v2,"Rue de la paix").afficher();

///////////////////////////////////////////////////
// Class Vue

function Com(){

    this.appelService = function(nomService, params, fonctionRetour){
        var xmlhttp=new XMLHttpRequest();
        xmlhttp.open("POST","http://localhost:4500/"+nomService,false);
        //xmlhttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
        xmlhttp.overrideMimeType('text/xml');

        var msg = "";
        for( var i = 0; i < params.length; ++i){
            msg += params[i];
        }
        xmlhttp.send(msg); // bloquant
        fonctionRetour(xmlhttp.responseText);
    }

    this.envoyerXml = function(fileEvt, nomService, fonctionRetour){
        var f = fileEvt.target.files[0];
        if(f){
            var extension = f.name.split('.').pop();
            if(extension === "xml" || extension === "XML"){
                var reader = new FileReader();
                
                reader.onload = function(e){
                    this.appelService(nomService,[e.target.result],fonctionRetour);
                }.bind(this);
                reader.readAsText(f);
            } else {
                vue.erreur("Le fichier sélectionné (."+extension+") n'a pas la bonne extension (.xml) !");
            }
        }
    }.bind(this);
}