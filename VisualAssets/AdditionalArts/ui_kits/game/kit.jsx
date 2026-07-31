// Shared helpers for the Quantum Effect game UI kit. Exported to window for cross-<script> access.
const DS = window.QuantumEffectDesignSystem_2d830e;

function Backdrop({image,children,dim=0.78}){
  return <div style={{position:'absolute',inset:0,overflow:'hidden'}}>
    <img src={image} alt="" style={{position:'absolute',inset:0,width:'100%',height:'100%',objectFit:'cover',imageRendering:'pixelated'}}/>
    <div style={{position:'absolute',inset:0,background:`rgba(4,6,13,${dim})`,backgroundImage:'var(--scanline)'}}></div>
    <div style={{position:'relative',height:'100%'}}>{children}</div>
  </div>;
}
const ICON='../../assets/icons/';
function Coin({children}){return <span style={{color:'var(--atom-gold)',display:'inline-flex',alignItems:'center',gap:5}}><img src={ICON+'coin-gold.png'} style={{width:18,height:18,imageRendering:'pixelated'}}/>{children}</span>;}
function Shard({children}){return <span style={{color:'var(--teal)',display:'inline-flex',alignItems:'center',gap:5}}><img src={ICON+'gem-blue.png'} style={{width:18,height:18,imageRendering:'pixelated'}}/>{children}</span>;}

Object.assign(window,{DS,Backdrop,Coin,Shard,ICON});
