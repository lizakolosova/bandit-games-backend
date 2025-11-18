# Documentation 
So guys if you are lost, we have 5 bounded contexts:
3 of them are shown in the domain model and 2 are external service: AI player and the game they give us

# Commits 
## Every commit message follows this pattern:
```
<type>(optional scope): <short summary> (#issueNumber)

(optional body)

(optional footer)
```

## Allowed Commit Types
### Use one of the following types:
- feat — a new feature
- fix — a bug fix
- refactor — code changes that don’t change behavior
- docs — documentation updates
- style — formatting only, no code logic changes (CSS tweaks, lint fixes…)
- test — adding or updating tests
- chore — configuration, dependency updates, build scripts
- perf — performance improvements
- build — changes to the build system (Gradle, Vite, Webpack…)
- ci — changes to CI/CD pipelines

## What Is a Scope
The optional(OPTIONAL so you don't need to put it, but it would be nice if you do) scope describes where the change happened.

#### Examples:
- feat(auth): ...
- fix(orders): ...
- refactor(game-engine): ...
- style(ui): ...

You can leave it out if it doesn’t make sense to put it.
## Referencing GitLab Issues (Important!)
To link your commit to a GitLab issue (so everyone knows what you are working on), add the issue number this way:
```
feat(game): add AI difficulty selection (#12)
```
You can even automatically close issue with this:
```
feat(game): add AI difficulty selection
closes #12
```
The commit that is influencing multiple issues:
```
refactor(core): simplify game state logic (#14, #18)
```
# Examples of the commit messages
```
! ✔ New Feature
feat(game): add AI difficulty selection (#14)
! ✔ Bug Fix
fix(orders): correct total price calculation (#23)
! ✔ Refactor
refactor(game-state): extract state validation logic (#8)
! ✔ Documentation
docs(readme): improve project setup section (#2)
! ✔ Style / UI
style(ui): adjust spacing on restaurant list page (#4)
! ✔ Tests
test(auth): add JWT validation tests (#7)
! ✔ Build / Config
chore(gradle): add Spring Modulith BOM (#9)
```

### ✅ Commit Checklist (for ChatGPT to generate your message if you are lazy)

Before committing, make sure:

- [x] You chose the correct type

- [x] Optional: you added a scope describing the part of the project

- [x] You referenced the correct issue number (#ID)

- [x] Your summary is short, clear, and imperative

- [x] Your change fits the meaning of the chosen type

- [x] You didn’t include unnecessary details in the first line