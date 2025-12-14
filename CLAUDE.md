# File Nesting Config - Plugin JetBrains

## Contexte

Plugin créé pour reproduire le comportement de [antfu/vscode-file-nesting-config](https://github.com/antfu/vscode-file-nesting-config) dans les IDE JetBrains (WebStorm, IntelliJ IDEA).

### Problème résolu

Le file nesting natif de JetBrains ne supporte que le regroupement par **suffixe** (ex: `app.ts` → `app.js`, `app.map`). Il ne permet **pas** de grouper des fichiers arbitraires comme `package.json` avec `.eslintrc`, `tsconfig.json`, etc.

Ce plugin utilise l'API `TreeStructureProvider` pour modifier l'arborescence du Project View et permettre ce type de regroupement avancé.

## Architecture

```
src/main/kotlin/com/github/vmaerten/filenesting/
├── FileNestingTreeStructureProvider.kt   # Point d'entrée - modifie le Project View
├── NestingGroupNode.kt                   # Nœud custom pour afficher parent + enfants
├── patterns/
│   ├── NestingPattern.kt                 # Data class (parent, children)
│   ├── PatternMatcher.kt                 # Matching avec wildcards (*.ts, .eslint*)
│   └── DefaultPatterns.kt                # ~50 patterns portés de vscode-file-nesting-config
└── settings/
    ├── FileNestingSettings.kt            # État persistant (enabled: true/false)
    └── FileNestingConfigurable.kt        # UI dans Settings > Appearance > File Nesting
```

## Points techniques importants

### TreeStructureProvider

L'extension point `com.intellij.treeStructureProvider` permet de modifier les nœuds du Project View. La méthode `modify()` reçoit les enfants d'un dossier et peut les réorganiser.

```kotlin
override fun modify(
    parent: AbstractTreeNode<*>,
    children: Collection<AbstractTreeNode<*>>,
    settings: ViewSettings
): Collection<AbstractTreeNode<*>>
```

### Pattern Matching

Les patterns supportent les wildcards :
- `*` → n'importe quelle séquence de caractères
- `$(capture)` → capture le basename pour les patterns de fichiers compilés

Exemples :
- `package.json` → `.eslint*`, `tsconfig.*`, `*.lock`
- `*.ts` → `$(capture).js`, `$(capture).d.ts`

### NestingGroupNode

Classe custom qui wrap un `PsiFileNode` parent et affiche ses enfants comme sous-nœuds. Affiche `+N` pour indiquer le nombre de fichiers nestés.

## Commandes

```bash
# Build
./gradlew build

# Lancer IDE de test
./gradlew runIde

# Générer le ZIP pour publication
./gradlew buildPlugin

# Publier sur JetBrains Marketplace
./gradlew publishPlugin
```

## Configuration

- **Cible** : IntelliJ 2025.1 → 2025.3 (`sinceBuild = "251"`, `untilBuild = "253.*"`)
- **Kotlin** : 2.1.0
- **Java** : 21
- **IntelliJ Platform Gradle Plugin** : 2.2.1

## Fichiers clés à modifier

| Besoin | Fichier |
|--------|---------|
| Ajouter des patterns | `DefaultPatterns.kt` |
| Modifier la logique de nesting | `FileNestingTreeStructureProvider.kt` |
| Changer les versions cibles | `gradle.properties` + `build.gradle.kts` |
| Modifier l'UI settings | `FileNestingConfigurable.kt` |

## Limitations connues

1. Les patterns `$(capture)` sont basiques - ils capturent juste le basename avant l'extension
2. Pas de configuration utilisateur custom (patterns hardcodés)
3. Pas de multi-level nesting (comme le natif JetBrains)

## Améliorations possibles

- [ ] Ajouter une UI pour configurer des patterns custom
- [ ] Supporter le multi-level nesting
- [ ] Ajouter des tests unitaires pour PatternMatcher
- [ ] Publier sur JetBrains Marketplace
