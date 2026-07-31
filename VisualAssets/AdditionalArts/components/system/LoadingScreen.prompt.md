Loading & transition screen (HUD & UI Kit §11).

```jsx
<LoadingScreen progress={72} status="Loading…"
  tip="Use Status Effects to gain an advantage in battle. They can turn the tide!"/>
```

Drop `tip` for a bare progress screen. Drive `progress` from real load state; the bar animates on `--dur-slow`.
