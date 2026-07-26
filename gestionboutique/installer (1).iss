; ============================================================
; Script Inno Setup — Installeur GestionBoutique
; ============================================================
; Prérequis : avoir déjà généré le dossier "GestionBoutique\" avec jpackage
; (--type app-image), place ce fichier installer.iss juste à côté de ce
; dossier (à la racine du projet), puis compile-le avec Inno Setup.
; ============================================================

[Setup]
AppName=GestionBoutique
AppVersion=1.0
AppPublisher=TonNom
AppPublisherURL=
DefaultDirName={autopf}\GestionBoutique
DefaultGroupName=GestionBoutique
UninstallDisplayIcon={app}\GestionBoutique.exe
OutputDir=installeur
OutputBaseFilename=GestionBoutique-Setup-1.0
Compression=lzma2
SolidCompression=yes
WizardStyle=modern
DisableProgramGroupPage=yes
ArchitecturesInstallIn64BitMode=x64compatible
PrivilegesRequired=lowest

[Languages]
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Files]
; Copie tout le contenu du dossier genere par jpackage (exe + runtime Java embarque)
Source: "GestionBoutique\*"; DestDir: "{app}"; Flags: recursesubdirs createallsubdirs

[Icons]
Name: "{group}\GestionBoutique"; Filename: "{app}\GestionBoutique.exe"
Name: "{autodesktop}\GestionBoutique"; Filename: "{app}\GestionBoutique.exe"; Tasks: desktopicon
Name: "{group}\Désinstaller GestionBoutique"; Filename: "{uninstallexe}"

[Tasks]
Name: "desktopicon"; Description: "Créer un raccourci sur le Bureau"; GroupDescription: "Raccourcis :"; Flags: unchecked

[Run]
Filename: "{app}\GestionBoutique.exe"; Description: "Lancer GestionBoutique"; Flags: nowait postinstall skipifsilent

[UninstallDelete]
; Supprime aussi le dossier de base de donnees local cree au premier lancement
Type: filesandordirs; Name: "{app}\database"
