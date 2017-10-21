/**
 * @author alexk
 * classes in this package access the board and give [Player]s the option
 * to interact with it. The [Coreprotocol] accesses protocol.actions classes
 * to allow [Player] input to be converted into actions
 */
package protocol.actions;

/*
 * LIST OF TRIGGERS AND RESPECTIVE REQUIRED LOCATION INPUTS
 * TRIGGERS ARE LISTED AS @throws in the javadoc
 * 
 * PSA: all passive triggers come in deck/hand/grave/removed variations (Board.triggerExecutableEffect)
 * 		example: when "turnstart" is triggered, "turnstarthand", "turnstartgrave", "turnstartdeck" and "turnstartremoved"
 * 				 are triggered as well
 * 
 * "entry": triggers a creature's effect when it's played
 * -p = current player
 * -loc[0] = location, the creature is played at (own location)
 * 
 * "turnstart": occurs at the start of each player's turn
 * -p = current player
 * -loc[0] = own location
 * 
 * "damaged": triggers a creature's effect when it's damaged
 * -p = current player
 * -loc[0] = own location
 * -loc[1] = location of the damage source
 * 
 * "creaturedamaged": occurs when a creature is damaged
 * -p = owner of the creature
 * -loc[0] = own location
 * -loc[1] = damaged creature
 * -loc[2] = damage source
 * 
 * "attack": triggers a creature's effect when it fights
 * -p = current player
 * -loc[0] = own location
 * -loc[1] = other creature's location
 * 
 * "creatureattack": occurs when a creature attacks
 * -p = current player
 * -loc[0] = own location
 * -loc[1] = attacking creature
 * -loc[2] = attack target
 * 
 * "move": triggers a creature's effect when it moves (creature already at new location)
 * -p = current player
 * -loc[0] = old location
 * -loc[1] = new location
 * 
 * "creaturemove": occurs when a creature moves (creature already at new location)
 * -p = current player
 * -loc[0] = old location
 * -loc[1] = new location
 * 
 */