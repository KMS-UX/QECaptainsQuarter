/** Radar-style minimap. Three shapes: circular (compass ring + N marker), square (framed), fog-of-war (radial fade). Blips are typed markers positioned in 0–1 space. */
export interface MinimapBlip {
  kind: 'player' | 'ally' | 'enemy' | 'objective' | 'poi';
  x: number; // 0–1 across width
  y: number; // 0–1 down height
}
export interface MinimapProps {
  shape?: 'circular' | 'square' | 'fog';
  size?: number;
  blips?: MinimapBlip[];
  fog?: boolean;
  heading?: number;
  label?: string;
  style?: React.CSSProperties;
}
export declare function Minimap(props: MinimapProps): JSX.Element;
