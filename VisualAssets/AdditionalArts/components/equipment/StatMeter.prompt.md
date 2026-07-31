Augmentation/ship stat meters + insertable mod chips (Modifications & Equipment sheet).

```jsx
<StatMeter label="Offense" value={86} color="var(--bar-hp)"/>
<StatMeter label="Firepower" value={85} color="var(--alert-red)"/>

<ModChip name="Critical Boost" effect="Increases critical rate by 15%." rarity="epic"/>
<ModChip name="Life Drain" effect="Restore HP when dealing damage." rarity="rare"/>
```

`StatMeter` is a generic labeled percentage bar (use it for augmentation Offense/Defense/Mobility/Utility/Resonance and ship Firepower/Defense/Maneuver/Cargo/Warp) — pass any `color`. `ModChip` is the rarity-framed insertable-mod row; `rarity` drives the frame + name color.
