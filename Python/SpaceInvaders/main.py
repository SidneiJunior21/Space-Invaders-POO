import pygame
from sprites import SpaceShip, Bunker 
from controller import EnemyController

pygame.init()
SCREEN_WIDTH = 800
SCREEN_HEIGHT = 600
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
pygame.display.set_caption("Space Invaders")
clock = pygame.time.Clock()
FPS = 60
BLACK = (0, 0, 0)

running = True
while running:
    
    all_sprites = pygame.sprite.Group()
    player = SpaceShip(SCREEN_WIDTH, SCREEN_HEIGHT, all_sprites)
    enemy_controller = EnemyController(SCREEN_WIDTH, SCREEN_HEIGHT)

    bunkers = pygame.sprite.Group()
    bunker_positions = [SCREEN_WIDTH * 0.2, SCREEN_WIDTH * 0.5, SCREEN_WIDTH * 0.8]
    for pos_x in bunker_positions:
        bunker = Bunker(pos_x, 480)
        bunkers.add(bunker)

    all_sprites.add(player)
    all_sprites.add(enemy_controller.enemies)
    all_sprites.add(bunkers)
    
    game_active = True
    
    while game_active:
        clock.tick(FPS)
        
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                game_active = False
                running = False

        all_sprites.update() 
        enemy_controller.update()
        
        all_sprites.add(enemy_controller.projectiles)

        pygame.sprite.groupcollide(player.projectiles, enemy_controller.enemies, True, True)
        
        player_foi_atingido = pygame.sprite.spritecollide(player, enemy_controller.projectiles, True)

        bunkers_hit_by_player = pygame.sprite.groupcollide(player.projectiles, bunkers, True, False)
        for projectiles in bunkers_hit_by_player.values():
            for bunker_atingido in projectiles:
                bunker_atingido.take_damage()
            
        bunkers_hit_by_enemies = pygame.sprite.groupcollide(enemy_controller.projectiles, bunkers, True, False)
        for projectiles in bunkers_hit_by_enemies.values():
            for bunker_atingido in projectiles:
                bunker_atingido.take_damage()
        
        if player_foi_atingido or not enemy_controller.enemies:
            if player_foi_atingido:
                print("Jogador atingido! Reiniciando...")
            else:
                print("Inimigos derrotados! Reiniciando...")
            
            game_active = False
            
        screen.fill(BLACK)
        all_sprites.draw(screen)

        pygame.display.flip()

pygame.quit()