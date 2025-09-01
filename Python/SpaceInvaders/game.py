import pygame
import sys
import ctypes
import settings
from sprites.player import SpaceShip
from sprites.bunker import Bunker
from sprites.enemy import EnemyController

class Game:
    def __init__(self):
        pygame.init()
        self._get_screen_dimensions()
        self.screen = pygame.display.set_mode((self.screen_width, self.screen_height), pygame.NOFRAME)
        pygame.display.set_caption(settings.TITLE)
        self.clock = pygame.time.Clock()
        self.running = True

    def _get_screen_dimensions(self):
        try:
            user32 = ctypes.windll.user32
            self.screen_width = user32.GetSystemMetrics(0)
            self.screen_height = user32.GetSystemMetrics(1)
        except (ImportError, AttributeError):
            info = pygame.display.Info()
            self.screen_width = info.current_w
            self.screen_height = info.current_h

    def _show_main_menu(self):
        self.screen.fill(settings.BLACK)
        font_title = pygame.font.Font(None, 74)
        font_button = pygame.font.Font(None, 50)
        font_controls = pygame.font.Font(None, 32)

        title_text = font_title.render("SPACE INVADERS", True, settings.WHITE)
        title_rect = title_text.get_rect(center=(self.screen_width / 2, self.screen_height * 0.3))

        start_text = font_button.render("Iniciar Jogo", True, settings.WHITE)
        start_rect = start_text.get_rect(center=(self.screen_width / 2, self.screen_height * 0.6))

        quit_text = font_button.render("Sair", True, settings.WHITE)
        quit_rect = quit_text.get_rect(center=(self.screen_width / 2, self.screen_height * 0.7))

        self.screen.blit(title_text, title_rect)
        self.screen.blit(start_text, start_rect)
        self.screen.blit(quit_text, quit_rect)

        controls_lines = [
            "Setas ou A/D : Mover",
            "Espaço : Atirar",
            "ESC : Pausar"
        ]
        
        start_y = self.screen_height * 0.85 
        line_spacing = 35

        for i, line in enumerate(controls_lines):
            line_surface = font_controls.render(line, True, settings.WHITE)
            line_rect = line_surface.get_rect(centerx=self.screen_width / 2, top=start_y + (i * line_spacing))
            self.screen.blit(line_surface, line_rect)

        pygame.display.flip()
        
        menu_active = True
        while menu_active:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self.running = False
                    menu_active = False
                if event.type == pygame.MOUSEBUTTONDOWN:
                    mouse_pos = pygame.mouse.get_pos()
                    if start_rect.collidepoint(mouse_pos):
                        menu_active = False
                    if quit_rect.collidepoint(mouse_pos):
                        self.running = False
                        menu_active = False
            self.clock.tick(settings.FPS)

    def _draw_pause_menu(self):
        overlay = pygame.Surface((self.screen_width, self.screen_height), pygame.SRCALPHA)
        overlay.fill((0, 0, 0, 150))
        self.screen.blit(overlay, (0, 0))
        font = pygame.font.Font(None, 50)
        
        continue_text = font.render("Continuar", True, settings.WHITE)
        restart_text = font.render("Reiniciar", True, settings.WHITE)
        quit_text = font.render("Sair", True, settings.WHITE)

        continue_rect = continue_text.get_rect(center=(self.screen_width / 2, self.screen_height / 2 - 60))
        restart_rect = restart_text.get_rect(center=(self.screen_width / 2, self.screen_height / 2))
        quit_rect = quit_text.get_rect(center=(self.screen_width / 2, self.screen_height / 2 + 60))

        self.screen.blit(continue_text, continue_rect)
        self.screen.blit(restart_text, restart_rect)
        self.screen.blit(quit_text, quit_rect)
        pygame.display.flip()
        
        return continue_rect, restart_rect, quit_rect

    def _run_game_session(self):
        all_sprites = pygame.sprite.Group()
        
        enemy_width = self.screen_width * 0.025
        enemy_height = self.screen_height * 0.05
        enemy_size = (enemy_width, enemy_height)
        spacing_x = self.screen_width * 0.05
        spacing_y = self.screen_height * 0.065
        enemy_spacing = (spacing_x, spacing_y)
        
        player_width = self.screen_width * 0.05
        player_height = self.screen_height * 0.04
        player_size = (player_width, player_height)

        bunker_width = self.screen_width * 0.1
        bunker_height = self.screen_height * 0.05
        bunker_size = (bunker_width, bunker_height)

        player = SpaceShip(self.screen_width, self.screen_height, all_sprites, player_size)
        enemy_controller = EnemyController(self.screen_width, self.screen_height, all_sprites, enemy_size, enemy_spacing, settings.ENEMY_ROWS, settings.ENEMY_COLS)
        
        bunkers = pygame.sprite.Group()
        bunker_y_pos = self.screen_height * 0.8 
        bunker_positions = [self.screen_width * 0.2, self.screen_width * 0.5, self.screen_width * 0.8]
        for pos_x in bunker_positions:
            bunker = Bunker(pos_x, bunker_y_pos, bunker_size)
            bunkers.add(bunker)

        all_sprites.add(player)
        all_sprites.add(bunkers)
        
        game_active = True
        paused = False

        while game_active:
            self.clock.tick(settings.FPS)
            
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    game_active = False
                    self.running = False
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_ESCAPE:
                        paused = not paused

            if paused:
                continue_rect, restart_rect, quit_rect = self._draw_pause_menu()
                pause_loop = True
                while pause_loop:
                    for event in pygame.event.get():
                        if event.type == pygame.QUIT:
                            pause_loop = False
                            game_active = False
                            self.running = False
                        if event.type == pygame.KEYDOWN:
                            if event.key == pygame.K_ESCAPE:
                                paused = False
                                pause_loop = False
                        if event.type == pygame.MOUSEBUTTONDOWN:
                            mouse_pos = pygame.mouse.get_pos()
                            if continue_rect.collidepoint(mouse_pos):
                                paused = False
                                pause_loop = False
                            if restart_rect.collidepoint(mouse_pos):
                                pause_loop = False
                                game_active = False
                            if quit_rect.collidepoint(mouse_pos):
                                pause_loop = False
                                game_active = False
                                self.running = False
            
            if not paused:
                all_sprites.update() 
                enemy_controller.update()
                
                pygame.sprite.groupcollide(player.projectiles, enemy_controller.enemies, True, True)
                if pygame.sprite.spritecollide(player, enemy_controller.projectiles, True) or not enemy_controller.enemies:
                    game_active = False
                
                bunkers_hit_by_player = pygame.sprite.groupcollide(player.projectiles, bunkers, True, False)
                for projectile, hit_bunkers in bunkers_hit_by_player.items():
                    for bunker in hit_bunkers:
                        bunker.take_damage()
                
                bunkers_hit_by_enemies = pygame.sprite.groupcollide(enemy_controller.projectiles, bunkers, True, False)
                for projectile, hit_bunkers in bunkers_hit_by_enemies.items():
                    for bunker in hit_bunkers:
                        bunker.take_damage()

            self.screen.fill(settings.BLACK)
            all_sprites.draw(self.screen)
            pygame.display.flip()

    def run(self):
        while self.running:
            self._show_main_menu()
            if self.running:
                self._run_game_session()
        
        pygame.quit()
        sys.exit()