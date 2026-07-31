function MainMenu({onPlay}){
  const {MenuList}=window.DS;
  const [sel,setSel]=React.useState('Continue');
  const items=["Continue","New Game","Load Game","Settings","Credits","Quit"];
  return <window.Backdrop image="../../assets/keyart-hero-city.png" dim={0.62}>
    <div style={{height:'100%',display:'flex',flexDirection:'column',justifyContent:'center',paddingLeft:80,gap:32}}>
      <div>
        <img src="../../assets/logo-gold.png" alt="Quantum Effect" style={{height:96}}/>
        <div style={{fontFamily:'var(--font-display)',fontSize:12,letterSpacing:'.3em',color:'var(--teal)',marginTop:12,textTransform:'uppercase'}}>The Quantum is Power · Power Corrupts · Choice Defines</div>
      </div>
      <div onClick={e=>{const t=e.target.textContent;if(t){setSel(t);if(t==='Continue'||t==='New Game')onPlay&&onPlay();}}}>
        <MenuList items={items} selected={sel} width={220}/>
      </div>
      <div style={{fontSize:11,color:'var(--text-faint)'}}>SAVE 01 · Aurin · Lv. 23 · Neo-Solis · 42:18 played</div>
    </div>
    <div style={{position:'absolute',bottom:20,right:28,fontSize:10,color:'var(--text-faint)',letterSpacing:'.1em'}}>QUANTUM EFFECT · BUILD 0.7.7 · © FIRST EARTH INTERACTIVE</div>
  </window.Backdrop>;
}
window.MainMenu=MainMenu;
